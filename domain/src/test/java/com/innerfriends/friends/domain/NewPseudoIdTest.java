package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

public class NewPseudoIdTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(NewPseudoId.class).verify();
    }

}
