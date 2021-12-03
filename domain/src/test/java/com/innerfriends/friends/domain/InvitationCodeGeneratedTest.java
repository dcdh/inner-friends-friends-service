package com.innerfriends.friends.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class InvitationCodeGeneratedTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(InvitationCodeGenerated.class).verify();
    }

    @Test
    public void should_be_valid_when_generated_at_is_before_15_minutes() {
        // Given
        final LocalDateTime generatedAt = LocalDateTime.now(ZoneOffset.UTC);
        final InvitationCodeGenerated invitationCodeGenerated = new InvitationCodeGenerated(
                mock(FromFriendId.class), mock(InvitationCode.class),
                new GeneratedAt(generatedAt));
        final LocalDateTimeProvider localDateTimeProvider = mock(LocalDateTimeProvider.class);
        doReturn(generatedAt.plusMinutes(12l)).when(localDateTimeProvider).now();

        // When
        final Boolean isValid = invitationCodeGenerated.isValid(localDateTimeProvider);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    public void should_not_be_valid_when_generated_at_is_at_15_minutes() {
        // Given
        final LocalDateTime generatedAt = LocalDateTime.now(ZoneOffset.UTC);
        final InvitationCodeGenerated invitationCodeGenerated = new InvitationCodeGenerated(
                mock(FromFriendId.class), mock(InvitationCode.class),
                new GeneratedAt(generatedAt));
        final LocalDateTimeProvider localDateTimeProvider = mock(LocalDateTimeProvider.class);
        doReturn(generatedAt.plusMinutes(15l)).when(localDateTimeProvider).now();

        // When
        final Boolean isValid = invitationCodeGenerated.isValid(localDateTimeProvider);

        // Then
        assertThat(isValid).isFalse();
    }

}
