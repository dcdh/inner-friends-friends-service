package com.innerfriends.friends.infrastructure.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipFromFriendWithToFriendCommand;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipFromFriendWithToFriendUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.outbox.FromFriendEstablishedAFriendshipWithToFriendEvent;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedEstablishAFriendshipFromFriendWithToFriendUseCase implements UseCase<Friend, EstablishAFriendshipFromFriendWithToFriendCommand> {

    private final EstablishAFriendshipFromFriendWithToFriendUseCase establishAFriendshipFromFriendWithToFriendUseCase;
    private final Event<ExportedEvent<?, ?>> event;
    private final ObjectMapper objectMapper;
    private final InstantProvider instantProvider;

    public ManagedEstablishAFriendshipFromFriendWithToFriendUseCase(final EstablishAFriendshipFromFriendWithToFriendUseCase establishAFriendshipFromFriendWithToFriendUseCase,
                                                                    final Event<ExportedEvent<?, ?>> event,
                                                                    final ObjectMapper objectMapper,
                                                                    final InstantProvider instantProvider) {
        this.establishAFriendshipFromFriendWithToFriendUseCase = Objects.requireNonNull(establishAFriendshipFromFriendWithToFriendUseCase);
        this.event = Objects.requireNonNull(event);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @Transactional
    @Override
    public Friend execute(final EstablishAFriendshipFromFriendWithToFriendCommand command) {
        final Friend friendConnected = establishAFriendshipFromFriendWithToFriendUseCase.execute(command);
        event.fire(FromFriendEstablishedAFriendshipWithToFriendEvent.of(friendConnected, objectMapper, instantProvider));
        return friendConnected;
    }
}
