package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class GenerateInvitationCodeUseCaseTest {

    private InvitationCodeGeneratedRepository invitationCodeGeneratedRepository;
    private InvitationCodeGenerator invitationCodeGenerator;
    private GeneratedAtProvider generatedAtProvider;

    @BeforeEach
    public void setup() {
        invitationCodeGeneratedRepository = mock(InvitationCodeGeneratedRepository.class);
        invitationCodeGenerator = mock(InvitationCodeGenerator.class);
        generatedAtProvider = mock(GeneratedAtProvider.class);
    }

    @Test
    public void should_generate_invitation_code() {
        // Given
        final GenerateInvitationCodeUseCase generateInvitationCodeUseCase = new GenerateInvitationCodeUseCase(invitationCodeGeneratedRepository, invitationCodeGenerator, generatedAtProvider);
        final InvitationCode invitationCode = mock(InvitationCode.class);
        doReturn(invitationCode).when(invitationCodeGenerator).generate(new FromFriendId("Mario"));
        final GeneratedAt generatedAt = new GeneratedAt(LocalDateTime.now(ZoneOffset.UTC));
        doReturn(generatedAt).when(generatedAtProvider).now();

        // When && Then
        assertThat(generateInvitationCodeUseCase.execute(new GenerateInvitationCodeCommand(new FromFriendId("Mario"))))
                .isEqualTo(new InvitationCodeGenerated(new FromFriendId("Mario"), invitationCode, generatedAt));
    }

}
