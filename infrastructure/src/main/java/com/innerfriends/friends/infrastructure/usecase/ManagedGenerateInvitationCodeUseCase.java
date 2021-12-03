package com.innerfriends.friends.infrastructure.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.domain.InvitationCodeGenerated;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.GenerateInvitationCodeCommand;
import com.innerfriends.friends.domain.usecase.GenerateInvitationCodeUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.outbox.InvitationCodeGeneratedEvent;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedGenerateInvitationCodeUseCase implements UseCase<InvitationCodeGenerated, GenerateInvitationCodeCommand> {

    private final GenerateInvitationCodeUseCase generateInvitationCodeUseCase;
    private final Event<ExportedEvent<?, ?>> event;
    private final ObjectMapper objectMapper;
    private final InstantProvider instantProvider;

    public ManagedGenerateInvitationCodeUseCase(final GenerateInvitationCodeUseCase generateInvitationCodeUseCase,
                                                final Event<ExportedEvent<?, ?>> event,
                                                final ObjectMapper objectMapper,
                                                final InstantProvider instantProvider) {
        this.generateInvitationCodeUseCase = Objects.requireNonNull(generateInvitationCodeUseCase);
        this.event = Objects.requireNonNull(event);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @Transactional
    @Override
    public InvitationCodeGenerated execute(final GenerateInvitationCodeCommand command) {
        final InvitationCodeGenerated invitationCodeGenerated = generateInvitationCodeUseCase.execute(command);
        event.fire(InvitationCodeGeneratedEvent.of(invitationCodeGenerated, objectMapper, instantProvider));
        return invitationCodeGenerated;
    }
}
