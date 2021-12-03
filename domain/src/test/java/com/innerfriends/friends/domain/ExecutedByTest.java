package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class ExecutedByTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(ExecutedBy.class).verify();
    }

}
