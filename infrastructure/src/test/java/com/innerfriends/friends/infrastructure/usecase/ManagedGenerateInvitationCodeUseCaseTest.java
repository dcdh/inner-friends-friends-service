package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.GeneratedAt;
import com.innerfriends.friends.domain.InvitationCode;
import com.innerfriends.friends.domain.InvitationCodeGenerated;
import com.innerfriends.friends.domain.usecase.GenerateInvitationCodeCommand;
import com.innerfriends.friends.domain.usecase.GenerateInvitationCodeUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class ManagedGenerateInvitationCodeUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedGenerateInvitationCodeUseCase managedGenerateInvitationCodeUseCase;

    @InjectMock
    GenerateInvitationCodeUseCase generateInvitationCodeUseCase;

    @InjectMock
    InstantProvider instantProvider;

    @Test
    public void should_generate_invitation_code() {
        // Given
        final InvitationCodeGenerated invitationCodeGenerated = new InvitationCodeGenerated(new FromFriendId("Mario"),
                new InvitationCode(new UUID(0, 0)), new GeneratedAt(LocalDateTime.now()));
        final GenerateInvitationCodeCommand generateInvitationCodeCommand = new GenerateInvitationCodeCommand(new FromFriendId("Mario"));
        doReturn(invitationCodeGenerated).when(generateInvitationCodeUseCase).execute(generateInvitationCodeCommand);
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When && Then
        assertThat(managedGenerateInvitationCodeUseCase.execute(generateInvitationCodeCommand))
                .isEqualTo(invitationCodeGenerated);
        assertThat(((Number) entityManager.createNativeQuery(COUNT_OUTBOX_EVENT_FOR_AGGREGATE_ID_SQL)
                .setParameter(1, "Mario")
                .getSingleResult()).longValue()).isEqualTo(1l);
        assertThat(entityManager.createNativeQuery(GET_TYPE_BY_AGGREGATE_ID)
                .setParameter(1, "Mario")
                .getSingleResult())
                .isEqualTo("InvitationCodeGenerated");
    }

}
