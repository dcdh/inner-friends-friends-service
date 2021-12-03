package com.innerfriends.friends.infrastructure;

import com.arangodb.ArangoDB;
import com.arangodb.entity.BaseDocument;
import com.innerfriends.friends.domain.*;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArangoDBFriends {

    private final ArangoDB arangoDB;

    private final String dbName;

    public ArangoDBFriends(final ArangoDB arangoDB,
                           @ConfigProperty(name = "arangodb.dbName") final String dbName) {
        this.arangoDB = arangoDB;
        this.dbName = dbName;
    }

    private void waitForFriendToBeRegistered(final String pseudoId) {
        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
                .until(() -> arangoDB.db(dbName).collection("FRIENDS").getDocument(pseudoId, BaseDocument.class) != null);
    }

    private void waitForRelationshipToBeCreated(final String from, final String to) {
        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
                .until(() -> arangoDB.db(dbName).collection("IN_FRIENDSHIP_WITH").getDocument(String.format("%s_%s", from, to), BaseDocument.class) != null);
    }

    public Friend getFriend(final String friendId) {
        waitForFriendToBeRegistered(friendId);
        final BaseDocument friend = arangoDB.db(dbName).collection("FRIENDS").getDocument(friendId, BaseDocument.class);
        final String query = "FOR t IN IN_FRIENDSHIP_WITH FILTER t._from == @from RETURN t._to";
        final Map<String, Object> bindVars = Map.of("from", "FRIENDS/" + friendId);
        final List<InFriendshipWithId> inFriendshipsWithId = arangoDB.db(dbName).query(query, bindVars, null, String.class)
                .stream()
                .map(to -> to.replace("FRIENDS/", ""))
                .map(InFriendshipWithId::new)
                .collect(Collectors.toList());
        return new Friend(
                new FriendId(friend.getKey()),
                inFriendshipsWithId,
                new Bio((String) friend.getAttribute("bio")),
                new Version((Long) friend.getAttribute("version"))
        );
    }

    public Friend getFriendWithRelationship(final String friendId, final String relationshipId) {
        waitForRelationshipToBeCreated(friendId, relationshipId);
        return getFriend(friendId);
    }

}
