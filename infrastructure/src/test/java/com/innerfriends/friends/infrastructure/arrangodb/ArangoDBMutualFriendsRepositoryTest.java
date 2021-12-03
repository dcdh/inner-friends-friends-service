package com.innerfriends.friends.infrastructure.arrangodb;

import com.arangodb.ArangoDB;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.BaseEdgeDocument;
import com.arangodb.entity.DocumentField;
import com.arangodb.model.EdgeCreateOptions;
import com.arangodb.model.VertexCreateOptions;
import com.innerfriends.friends.domain.*;
import io.quarkus.test.junit.QuarkusTest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class ArangoDBMutualFriendsRepositoryTest {

    public static final List<String> DELETES = List.of(
            "FOR f IN FRIENDS FILTER f._id != 'FRIENDS/DamDamDeo' REMOVE f IN FRIENDS",
            "FOR f IN IN_FRIENDSHIP_WITH REMOVE f IN IN_FRIENDSHIP_WITH");

    @Inject
    ArangoDBMutualFriendsRepository arangoDBMutualFriendsRepository;

    @Inject
    ArangoDB arangoDB;

    @ConfigProperty(name = "arangodb.dbName")
    String dbName;

    @BeforeEach
    @AfterEach
    public void flush() {
        DELETES
                .stream()
                .forEach(query -> arangoDB.db(dbName).query(query, Void.class));
    }

    @Test
    public void should_get_in_relationship_mutual_friends() {
        // Given
        createFriend("Mario");
        createFriend("DonkeyKong");
        createFriend("Pauline");
        createFriend("Luigi");
        createFriend("Toad");
        createFriend("Bowser");
        createRelationship("Mario", "DonkeyKong");
        createRelationship("DonkeyKong", "Pauline");
        createRelationship("DonkeyKong", "Luigi");
        createRelationship("Mario", "Pauline");
        createRelationship("Mario", "Luigi");
        createRelationship("Mario", "Toad");

        // When
        final List<MutualFriendId> mutualFriends = arangoDBMutualFriendsRepository.getMutualFriends(new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"));

        // Then
        assertThat(mutualFriends)
                .containsExactlyInAnyOrder(new MutualFriendId("Pauline"), new MutualFriendId("Luigi"));
    }

    @Test
    public void should_get_friends_of_friend_mutual_friends() {
        // Given
        createFriend("Mario");
        createFriend("DonkeyKong");
        createFriend("DiddyKong");
        createFriend("Pauline");
        createFriend("Luigi");
        createFriend("Toad");
        createFriend("Bowser");
        createRelationship("Mario", "DonkeyKong");
        createRelationship("DonkeyKong", "Pauline");
        createRelationship("DonkeyKong", "Luigi");
        createRelationship("Mario", "Pauline");
        createRelationship("Mario", "Luigi");
        createRelationship("Mario", "Toad");
        createRelationship("DonkeyKong", "DiddyKong");
        createRelationship("DiddyKong", "Luigi");

        // When
        final List<MutualFriendId> mutualFriends = arangoDBMutualFriendsRepository.getMutualFriends(new FriendId("Mario"),
                new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("DiddyKong"));

        assertThat(mutualFriends)
                .containsExactly(new MutualFriendId("Luigi"));
    }

    @Test
    public void should_register_a_new_friend_into_the_platform() {
        // Given

        // When
        arangoDBMutualFriendsRepository.registerANewFriendIntoThePlatform(new FriendId("Mario"),
                List.of(new InFriendshipWithId("DamDamDeo")),
                new Version(0l));

        // Then
        final BaseDocument relationship = getRelationship("Mario", "DamDamDeo");
        assertThat(relationship).isNotNull();
        assertThat(relationship.getAttribute(DocumentField.Type.FROM.getSerializeName())).isEqualTo("FRIENDS/Mario");
        assertThat(relationship.getAttribute(DocumentField.Type.TO.getSerializeName())).isEqualTo("FRIENDS/DamDamDeo");
    }

    @Test
    public void should_write_a_bio() {
        // Given
        createFriend("Mario");

        // When
        arangoDBMutualFriendsRepository.writeBio(new FriendId("Mario"), new Bio("super plumber"), new Version(1l));

        // Then
        final BaseDocument friend = getFriend("Mario");
        assertThat(friend).isNotNull();
        assertThat(friend.getAttribute("bio")).isEqualTo("super plumber");
        assertThat(friend.getAttribute("version")).isEqualTo(1l);
    }

    @Test
    public void should_establish_friendship_with() {
        // Given
        createFriend("Mario");
        createFriend("Luigi");

        // When
        arangoDBMutualFriendsRepository.establishFriendshipWith(
                new FriendId("Mario"),
                new EstablishedFriendshipWith(new InFriendshipWithId("Luigi")),
                new Version(1l));

        // Then
        final BaseDocument relationship = getRelationship("Mario", "Luigi");
        assertThat(relationship).isNotNull();
        assertThat(relationship.getAttribute(DocumentField.Type.FROM.getSerializeName())).isEqualTo("FRIENDS/Mario");
        assertThat(relationship.getAttribute(DocumentField.Type.TO.getSerializeName())).isEqualTo("FRIENDS/Luigi");
    }

    private void createFriend(final String pseudo) {
        final BaseDocument friend = new BaseDocument(pseudo);
        friend.addAttribute("bio", "");
        friend.addAttribute("version", 0l);
        arangoDB.db(dbName)
                .graph("FRIENDS")
                .vertexCollection("FRIENDS")
                .insertVertex(
                        friend,
                        new VertexCreateOptions().waitForSync(true));
    }

    private BaseDocument getFriend(final String pseudo) {
        return arangoDB.db(dbName)
                .graph("FRIENDS")
                .vertexCollection("FRIENDS")
                .getVertex(pseudo, BaseDocument.class);
    }

    private void createRelationship(final String from, final String to) {
        arangoDB.db(dbName)
                .graph("FRIENDS")
                .edgeCollection("IN_FRIENDSHIP_WITH")
                .insertEdge(
                        new BaseEdgeDocument(String.format("%s_%s", from, to), "FRIENDS/" + from, "FRIENDS/" + to),
                        new EdgeCreateOptions().waitForSync(true));
    }

    private BaseDocument getRelationship(final String from, final String to) {
        return arangoDB.db(dbName)
                .graph("FRIENDS")
                .edgeCollection("IN_FRIENDSHIP_WITH")
                .getEdge(String.format("%s_%s", from, to), BaseDocument.class);
    }

}
