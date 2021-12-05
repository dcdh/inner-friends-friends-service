package com.innerfriends.friends.infrastructure.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.RegisterNewFriendIntoThePlatformCommand;
import com.innerfriends.friends.domain.usecase.RegisterNewFriendIntoThePlatformUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.outbox.NewFriendRegisteredIntoThePlatformEvent;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedRegisterNewFriendIntoThePlatformUseCase implements UseCase<Friend, RegisterNewFriendIntoThePlatformCommand> {

    private final RegisterNewFriendIntoThePlatformUseCase registerNewFriendIntoThePlatformUseCase;
    private final Event<ExportedEvent<?, ?>> event;
    private final ObjectMapper objectMapper;
    private final InstantProvider instantProvider;

    public ManagedRegisterNewFriendIntoThePlatformUseCase(final RegisterNewFriendIntoThePlatformUseCase registerNewFriendIntoThePlatformUseCase,
                                                          final Event<ExportedEvent<?, ?>> event,
                                                          final ObjectMapper objectMapper,
                                                          final InstantProvider instantProvider) {
        this.registerNewFriendIntoThePlatformUseCase = Objects.requireNonNull(registerNewFriendIntoThePlatformUseCase);
        this.event = Objects.requireNonNull(event);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @Transactional
    @Override
    public Friend execute(final RegisterNewFriendIntoThePlatformCommand command) {
        final Friend newFriendRegistered = registerNewFriendIntoThePlatformUseCase.execute(command);
        event.fire(NewFriendRegisteredIntoThePlatformEvent.of(newFriendRegistered, objectMapper, instantProvider));
        return newFriendRegistered;
    }
}
