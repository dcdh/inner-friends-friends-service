package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(Friend.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void should_be_in_friendship_with_DamDamDeo() {
        assertThat(new Friend(new FriendId("Mario")))
                .isEqualTo(
                        new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("DamDamDeo")), new Bio(), new Version(0l)));
    }

    @Test
    public void should_return_nb_of_friends() {
        assertThat(new Friend(new FriendId("Mario")).nbOfFriends()).isEqualTo(1);
    }

    @Test
    public void should_return_bio() {
        assertThat(new Friend(new FriendId("Mario")).bio()).isEqualTo(new Bio());
    }

    @Test
    public void should_not_has_bio() {
        assertThat(new Friend(new FriendId("Mario")).hasBio()).isFalse();
    }

    @Test
    public void should_has_bio() {
        assertThat(new Friend(new FriendId("Mario"))
                .writeBio(new Bio("super plumber"), new ExecutedBy("Mario"))
                .hasBio()).isTrue();
    }

    @Test
    public void should_return_last_established_friendship_with() {
        assertThat(new Friend(new FriendId("Mario")).establishAFriendship(new FriendId("Donkey Kong"))
                .lastEstablishedFriendshipWith())
                .isEqualTo(new InFriendshipWithId("Donkey Kong"));
    }

    @Test
    public void should_not_established_friendship_with_DamDamDeo_has_already_done() {
        assertThat(new Friend(new FriendId("Mario")).establishAFriendship(new FriendId("DamDamDeo")))
                .isEqualTo(new Friend(new FriendId("Mario")));
    }
}
