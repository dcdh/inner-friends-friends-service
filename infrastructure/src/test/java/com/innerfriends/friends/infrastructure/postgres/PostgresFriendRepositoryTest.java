package com.innerfriends.friends.infrastructure.postgres;

import com.innerfriends.friends.domain.*;
import io.quarkus.test.junit.QuarkusTest;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.postgresql.util.PSQLException;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class PostgresFriendRepositoryTest extends RepositoryTesting {

    @Inject
    EntityManager entityManager;

    @Inject
    PostgresFriendRepository postgresFriendRepository;

    @Test
    public void should_create_friend() throws Exception {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));

        // When
        runInTransaction(() -> {
            postgresFriendRepository.create(friend);
            return null;
        });

        // Then
        assertThat(entityManager.find(FriendEntity.class, "Mario"))
                .isEqualTo(new FriendEntity("Mario", List.of("DamDamDeo"), "", 1, 0l));
    }

    @Test
    public void should_fail_to_create_friend_when_pseudo_is_already_used() throws Exception {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));
        runInTransaction(() -> {
            postgresFriendRepository.create(friend);
            return null;
        });

        // When && Then
        assertThatThrownBy(() -> runInTransaction(() -> {
            postgresFriendRepository.create(friend);
            return null;
        }))
                .isInstanceOf(PseudoAlreadyUsedException.class)
                .hasFieldOrPropertyWithValue("friendId", new FriendId("Mario"));
    }

    @Test
    public void should_save_friend() throws Exception {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));
        runInTransaction(() -> {
            entityManager.persist(new FriendEntity(new Friend(new FriendId("Mario"))));
            return null;
        });
        friend.writeBio(new Bio("super plumber"), new ExecutedBy("Mario"));

        // When
        runInTransaction(() -> {
            postgresFriendRepository.save(friend);
            return null;
        });

        // Then
        assertThat(entityManager.find(FriendEntity.class, "Mario"))
                .isEqualTo(new FriendEntity("Mario", List.of("DamDamDeo"), "super plumber", 1, 1l));
    }

    @Test
    public void should_fail_to_save_friend_when_next_version_is_not_incremented_by_one() throws Exception {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));
        runInTransaction(() -> {
            entityManager.persist(new FriendEntity(new Friend(new FriendId("Mario"))));
            return null;
        });
        friend.writeBio(new Bio("super plumber"), new ExecutedBy("Mario"));
        friend.writeBio(new Bio("Boom"), new ExecutedBy("Mario"));

        // When && Then
        assertThatThrownBy(() -> runInTransaction(() -> {
            postgresFriendRepository.save(friend);
            return null;
        }))
                .getRootCause()
                .isInstanceOf(PSQLException.class)
                .hasMessage("ERROR: Friend version unexpected on update for friendid Mario - current version 2 - expected version 1\n" +
                        "  Où : PL/pgSQL function friend_check_version_on_update() line 6 at RAISE");
    }

    @Test
    public void should_get_friend() throws Exception {
        // Given
        runInTransaction(() -> {
            entityManager.persist(new FriendEntity(new Friend(new FriendId("Mario"))));
            return null;
        });

        // When
        final Friend friend = runInTransaction(() -> postgresFriendRepository.getBy(new FriendId("Mario")));

        // Then
        assertThat(friend).isEqualTo(new Friend(new FriendId("Mario")));
    }

    @Test
    public void should_fail_to_get_friend_when_friend_does_not_exist() throws Exception {
        // Given

        // When && Then
        assertThatThrownBy(() -> runInTransaction(() -> postgresFriendRepository.getBy(new FriendId("Mario"))))
                .isInstanceOf(FriendUnknownException.class)
                .hasFieldOrPropertyWithValue("friendId", new FriendId("Mario"));
    }

}
