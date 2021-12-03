package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class VersionTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(Version.class).verify();
    }

    @Test
    public void should_fail_fast_when_version_is_negative() {
        assertThatThrownBy(() -> new Version(-1l))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Must be >= 0");
    }

    @Test
    public void should_increment() {
        assertThat(new Version(0l).increment()).isEqualTo(new Version(1l));
    }

}
