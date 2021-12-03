package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;

import java.util.Objects;

public class GenerateInvitationCodeUseCase implements UseCase<InvitationCodeGenerated, GenerateInvitationCodeCommand> {

    private final InvitationCodeGeneratedRepository invitationCodeGeneratedRepository;
    private final InvitationCodeGenerator invitationCodeGenerator;
    private final GeneratedAtProvider generatedAtProvider;

    public GenerateInvitationCodeUseCase(final InvitationCodeGeneratedRepository invitationCodeGeneratedRepository,
                                         final InvitationCodeGenerator invitationCodeGenerator,
                                         final GeneratedAtProvider generatedAtProvider) {
        this.invitationCodeGeneratedRepository = Objects.requireNonNull(invitationCodeGeneratedRepository);
        this.invitationCodeGenerator = Objects.requireNonNull(invitationCodeGenerator);
        this.generatedAtProvider = Objects.requireNonNull(generatedAtProvider);
    }

    @Override
    public InvitationCodeGenerated execute(final GenerateInvitationCodeCommand command) {
        final InvitationCode invitationCode = invitationCodeGenerator.generate(command.fromFriendId());
        final InvitationCodeGenerated invitationCodeGenerated = new InvitationCodeGenerated(
                command.fromFriendId(), invitationCode, generatedAtProvider.now());
        invitationCodeGeneratedRepository.store(invitationCodeGenerated);
        return invitationCodeGenerated;
    }

}
