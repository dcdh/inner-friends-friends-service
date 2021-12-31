package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class FriendMayKnowIdTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(FriendMayKnowId.class).verify();
    }

}