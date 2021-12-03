package com.innerfriends.friends.infrastructure.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipToFriendWithFromFriendCommand;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipToFriendWithFromFriendUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.outbox.ToFriendEstablishedAFriendshipWithFromFriendEvent;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedEstablishAFriendshipToFriendWithFromFriendUseCase implements UseCase<Friend, EstablishAFriendshipToFriendWithFromFriendCommand> {

    private final EstablishAFriendshipToFriendWithFromFriendUseCase establishAFriendshipToFriendWithFromFriendUseCase;
    private final Event<ExportedEvent<?, ?>> event;
    private final ObjectMapper objectMapper;
    private final InstantProvider instantProvider;

    public ManagedEstablishAFriendshipToFriendWithFromFriendUseCase(final EstablishAFriendshipToFriendWithFromFriendUseCase establishAFriendshipToFriendWithFromFriendUseCase,
                                                                    final Event<ExportedEvent<?, ?>> event,
                                                                    final ObjectMapper objectMapper,
                                                                    final InstantProvider instantProvider) {
        this.establishAFriendshipToFriendWithFromFriendUseCase = Objects.requireNonNull(establishAFriendshipToFriendWithFromFriendUseCase);
        this.event = Objects.requireNonNull(event);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @Transactional
    @Override
    public Friend execute(final EstablishAFriendshipToFriendWithFromFriendCommand command) {
        final Friend friendConnected = establishAFriendshipToFriendWithFromFriendUseCase.execute(command);
        event.fire(ToFriendEstablishedAFriendshipWithFromFriendEvent.of(friendConnected, objectMapper, instantProvider));
        return friendConnected;
    }
}
