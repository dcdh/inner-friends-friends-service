package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BioTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(Bio.class).verify();
    }

    @Test
    public void should_fail_fast_when_content_is_more_than_499_length() {
        // Given
        final StringBuilder bioTooLong = new StringBuilder(500);
        for (int i = 0; i < 500; i++) {
            bioTooLong.append("A");
        }

        // When && Then
        assertThatThrownBy(() -> new Bio(bioTooLong.toString())).isInstanceOf(IllegalStateException.class);
    }

}
