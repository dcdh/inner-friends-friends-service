package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class FriendIdTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(FriendId.class).verify();
    }

}
