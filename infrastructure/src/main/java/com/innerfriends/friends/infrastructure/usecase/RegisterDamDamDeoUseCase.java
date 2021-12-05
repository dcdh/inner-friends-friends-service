package com.innerfriends.friends.infrastructure.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendUnknownException;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.outbox.NewFriendRegisteredIntoThePlatformEvent;
import com.innerfriends.friends.infrastructure.postgres.PostgresFriendRepository;
import io.debezium.outbox.quarkus.ExportedEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class RegisterDamDamDeoUseCase implements UseCase<Void, RegisterDamDamDeoCommand> {

    private final PostgresFriendRepository postgresFriendRepository;
    private final Event<ExportedEvent<?, ?>> event;
    private final ObjectMapper objectMapper;
    private final InstantProvider instantProvider;

    public RegisterDamDamDeoUseCase(final PostgresFriendRepository postgresFriendRepository,
                                    final Event<ExportedEvent<?, ?>> event,
                                    final ObjectMapper objectMapper,
                                    final InstantProvider instantProvider) {
        this.postgresFriendRepository = Objects.requireNonNull(postgresFriendRepository);
        this.event = Objects.requireNonNull(event);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @Transactional
    @Override
    public Void execute(final RegisterDamDamDeoCommand command) {
        try {
            postgresFriendRepository.getBy(new FriendId(Friend.DAM_DAM_DEO.pseudoId()));
        } catch (final FriendUnknownException friendUnknownException) {
            final Friend damDamDeoFriendToCreate = new Friend(new FriendId(Friend.DAM_DAM_DEO.pseudoId()));
            event.fire(NewFriendRegisteredIntoThePlatformEvent.of(damDamDeoFriendToCreate, objectMapper, instantProvider));
            postgresFriendRepository.create(damDamDeoFriendToCreate);
        }
        return null;
    }

}
