package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneratedAtTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(GeneratedAt.class).verify();
    }

    @Test
    public void should_return_added_at() {
        assertThat(new GeneratedAt(buildLocalDateTime()).at()).isEqualTo(buildLocalDateTime());
    }

    private LocalDateTime buildLocalDateTime() {
        return LocalDateTime.of(2021, 10, 31, 0, 0, 0, 0);
    }

}
