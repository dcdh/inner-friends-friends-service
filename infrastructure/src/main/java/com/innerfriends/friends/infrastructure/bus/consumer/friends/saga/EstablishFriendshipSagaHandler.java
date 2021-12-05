package com.innerfriends.friends.infrastructure.bus.consumer.friends.saga;

import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.ToFriendId;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipFromFriendWithToFriendCommand;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.opentelemetry.NewSpan;
import com.innerfriends.friends.infrastructure.outbox.ToFriendEstablishedAFriendshipWithFromFriendEvent;
import com.innerfriends.friends.infrastructure.postgres.MessageLogRepository;
import com.innerfriends.friends.infrastructure.usecase.ManagedEstablishAFriendshipFromFriendWithToFriendUseCase;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class EstablishFriendshipSagaHandler {

    private static final Logger LOG = LoggerFactory.getLogger(EstablishFriendshipSagaHandler.class);

    private static final String FRIEND_ID = "friendId";
    private static final String ESTABLISHED_FRIENDSHIP_WITH = "establishedFriendshipWith";

    private final ManagedEstablishAFriendshipFromFriendWithToFriendUseCase managedEstablishAFriendshipFromFriendWithToFriendUseCase;
    private final MessageLogRepository messageLogRepository;
    private final InstantProvider instantProvider;

    public EstablishFriendshipSagaHandler(final ManagedEstablishAFriendshipFromFriendWithToFriendUseCase managedEstablishAFriendshipFromFriendWithToFriendUseCase,
                                          final MessageLogRepository messageLogRepository,
                                          final InstantProvider instantProvider) {
        this.managedEstablishAFriendshipFromFriendWithToFriendUseCase = Objects.requireNonNull(managedEstablishAFriendshipFromFriendWithToFriendUseCase);
        this.messageLogRepository = Objects.requireNonNull(messageLogRepository);
        this.instantProvider = Objects.requireNonNull(instantProvider);
    }

    @NewSpan
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public final void onEvent(final String groupId,
                              final UUID eventId,
                              final String eventType,
                              final String aggregateId,
                              final JsonObject eventPayload,
                              final Instant messageInstant) {
        LOG.info("Received event -- groupId: {}, aggregateId: {}, event id: '{}', event type: '{}', message timestamp: '{}'", groupId, aggregateId, eventId, eventType, messageInstant);

        if (messageLogRepository.alreadyProcessed(groupId, eventId)) {
            LOG.info("Event with UUID {} was already processed, ignoring it", eventId);
            return;
        }
        if (ToFriendEstablishedAFriendshipWithFromFriendEvent.TYPE.equals(eventType)) {
            managedEstablishAFriendshipFromFriendWithToFriendUseCase.execute(
                    new EstablishAFriendshipFromFriendWithToFriendCommand(new FromFriendId(eventPayload.getString(ESTABLISHED_FRIENDSHIP_WITH)),
                            new ToFriendId(eventPayload.getString(FRIEND_ID))));
        } else {
            LOG.info("Unknown event type '{}' for event id '{}', will be marked as processed.", eventType, eventId);
        }

        messageLogRepository.markAsConsumed(groupId, eventId, instantProvider.now());
    }

}
