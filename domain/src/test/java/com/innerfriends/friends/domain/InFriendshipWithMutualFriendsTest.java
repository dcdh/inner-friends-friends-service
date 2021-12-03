package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InFriendshipWithMutualFriendsTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(InFriendshipWithMutualFriends.class).verify();
    }

    @Test
    public void should_return_nb_of_mutual_friends() {
        assertThat(
                new InFriendshipWithMutualFriends(
                        new FriendId("Mario"), new InFriendshipWithId("Luigi"),
                        List.of(new MutualFriendId("Toad")))
                        .nbOfMutualFriends())
                .isEqualTo(1);
    }

    @Nested
    public static class FilterDuplicates {

        @Test
        public void should_remove_duplicates_from_mutual_friends_id() {
            assertThat(
                    new InFriendshipWithMutualFriends(
                            new FriendId("Mario"), new InFriendshipWithId("Luigi"),
                            List.of(new MutualFriendId("Toad"), new MutualFriendId("Toad")))
                            .mutualFriendsId())
                    .containsExactly(new MutualFriendId("Toad"));
        }

        @Test
        public void should_return_nb_of_mutual_friends_without_duplicates() {
            assertThat(
                    new InFriendshipWithMutualFriends(
                            new FriendId("Mario"), new InFriendshipWithId("Luigi"),
                            List.of(new MutualFriendId("Toad"), new MutualFriendId("Toad")))
                            .nbOfMutualFriends())
                    .isEqualTo(1);
        }

    }
}
