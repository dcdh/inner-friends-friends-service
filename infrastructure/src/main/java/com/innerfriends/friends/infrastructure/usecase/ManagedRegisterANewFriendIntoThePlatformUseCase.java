package com.innerfriends.friends.infrastructure.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.RegisterANewFriendIntoThePlatformCommand;
import com.innerfriends.friends.domain.usecase.RegisterANewFriendIntoThePlatformUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.outbox.ANewFriendRegisteredIntoThePlatformEvent;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedRegisterANewFriendIntoThePlatformUseCase implements UseCase<Friend, RegisterANewFriendIntoThePlatformCommand> {

    private final RegisterANewFriendIntoThePlatformUseCase registerANewFriendIntoThePlatformUseCase;
    private final Event<ExportedEvent<?, ?>> event;
    private final ObjectMapper objectMapper;
    private final InstantProvider instantProvider;

    public ManagedRegisterANewFriendIntoThePlatformUseCase(final RegisterANewFriendIntoThePlatformUseCase registerANewFriendIntoThePlatformUseCase,
                                                           final Event<ExportedEvent<?, ?>> event,
                                                           final ObjectMapper objectMapper,
                                                           final InstantProvider instantProvider) {
        this.registerANewFriendIntoThePlatformUseCase = Objects.requireNonNull(registerANewFriendIntoThePlatformUseCase);
        this.event = Objects.requireNonNull(event);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @Transactional
    @Override
    public Friend execute(final RegisterANewFriendIntoThePlatformCommand command) {
        final Friend newFriendRegistered = registerANewFriendIntoThePlatformUseCase.execute(command);
        event.fire(ANewFriendRegisteredIntoThePlatformEvent.of(newFriendRegistered, objectMapper, instantProvider));
        return newFriendRegistered;
    }
}
