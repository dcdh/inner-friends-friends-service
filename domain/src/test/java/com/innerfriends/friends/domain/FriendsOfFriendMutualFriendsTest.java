package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendsOfFriendMutualFriendsTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(FriendsOfFriendMutualFriends.class).verify();
    }

    @Test
    public void should_return_nb_of_mutual_friends() {
        Assertions.assertThat(
                new FriendsOfFriendMutualFriends(
                        new FriendId("Mario"), new InFriendshipWithId("Donkey Kong"),
                        new FriendOfFriendId("Pauline"), List.of(new MutualFriendId("Pauline")))
                        .nbOfMutualFriends()).isEqualTo(1);
    }

    @Nested
    public static class FilterDuplicates {

        @Test
        public void should_remove_duplicates_from_mutual_friends_id() {
            assertThat(
                    new FriendsOfFriendMutualFriends(
                            new FriendId("Mario"), new InFriendshipWithId("Luigi"),
                            new FriendOfFriendId("Peach"),
                            List.of(new MutualFriendId("Toad"), new MutualFriendId("Toad")))
                            .mutualFriendsId())
                    .containsExactly(new MutualFriendId("Toad"));
        }

        @Test
        public void should_return_nb_of_mutual_friends_without_duplicates() {
            assertThat(
                    new FriendsOfFriendMutualFriends(
                            new FriendId("Mario"), new InFriendshipWithId("Luigi"),
                            new FriendOfFriendId("Peach"),
                            List.of(new MutualFriendId("Toad"), new MutualFriendId("Toad")))
                            .nbOfMutualFriends())
                    .isEqualTo(1);
        }

    }

    @Nested
    public static class RemoveInFriendShipFromMutualFriends {

        @Test
        public void should_remove_in_friendship_with_from_mutual_friends_id() {
            assertThat(
                    new FriendsOfFriendMutualFriends(
                            new FriendId("Mario"), new InFriendshipWithId("Luigi"),
                            new FriendOfFriendId("Toad"),
                            List.of(new MutualFriendId("Peach"), new MutualFriendId("Luigi")))
                            .mutualFriendsId())
                    .containsExactly(new MutualFriendId("Peach"));
        }

        @Test
        public void should_return_nb_of_mutual_friends_without_in_friendship_with() {
            assertThat(
                    new FriendsOfFriendMutualFriends(
                            new FriendId("Mario"), new InFriendshipWithId("Luigi"),
                            new FriendOfFriendId("Toad"),
                            List.of(new MutualFriendId("Peach"), new MutualFriendId("Luigi")))
                            .nbOfMutualFriends())
                    .isEqualTo(1);
        }

    }

}