package com.innerfriends.friends.infrastructure.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.WriteBioCommand;
import com.innerfriends.friends.domain.usecase.WriteBioUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.outbox.BioWrittenEvent;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedWriteBioUseCase implements UseCase<Friend, WriteBioCommand> {

    private final WriteBioUseCase writeBioUseCase;
    private final Event<ExportedEvent<?, ?>> event;
    private final ObjectMapper objectMapper;
    private final InstantProvider instantProvider;

    public ManagedWriteBioUseCase(final WriteBioUseCase writeBioUseCase,
                                  final Event<ExportedEvent<?, ?>> event,
                                  final ObjectMapper objectMapper,
                                  final InstantProvider instantProvider) {
        this.writeBioUseCase = Objects.requireNonNull(writeBioUseCase);
        this.event = Objects.requireNonNull(event);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @Transactional
    @Override
    public Friend execute(final WriteBioCommand command) {
        final Friend friendWithBioWritten = writeBioUseCase.execute(command);
        event.fire(BioWrittenEvent.of(friendWithBioWritten, objectMapper, instantProvider));
        return friendWithBioWritten;
    }
}
