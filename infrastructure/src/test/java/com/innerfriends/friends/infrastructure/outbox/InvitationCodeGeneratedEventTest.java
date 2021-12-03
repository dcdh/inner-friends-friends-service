package com.innerfriends.friends.infrastructure.outbox;

import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.GeneratedAt;
import com.innerfriends.friends.domain.InvitationCode;
import com.innerfriends.friends.domain.InvitationCodeGenerated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.approvaltests.Approvals.verifyJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class InvitationCodeGeneratedEventTest extends EventTest {

    @Test
    public void should_return_expected_event() {
        // Given
        final InvitationCodeGenerated invitationCodeGenerated = new InvitationCodeGenerated(new FromFriendId("Mario"),
                new InvitationCode(new UUID(0, 0)),
                new GeneratedAt(LocalDateTime.of(2021, 11, 10, 0, 0 ,0, 0)));
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When
        final InvitationCodeGeneratedEvent invitationCodeGeneratedEvent = InvitationCodeGeneratedEvent.of(invitationCodeGenerated, objectMapper, instantProvider);

        // Then
        assertThat(invitationCodeGeneratedEvent.getAggregateId()).isEqualTo("Mario");
        assertThat(invitationCodeGeneratedEvent.getAggregateType()).isEqualTo("Friend");
        assertThat(invitationCodeGeneratedEvent.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1));
        assertThat(invitationCodeGeneratedEvent.getType()).isEqualTo("InvitationCodeGenerated");
        verifyJson(invitationCodeGeneratedEvent.getPayload().toString());
    }
}
