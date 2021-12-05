package com.innerfriends.friends.infrastructure.bus.consumer.friends.saga;

import com.innerfriends.friends.infrastructure.postgres.KafkaEventConsumer;
import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class EstablishFriendshipSagaConsumer extends KafkaEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(EstablishFriendshipSagaConsumer.class);

    private final EstablishFriendshipSagaHandler establishFriendshipSagaHandler;
    private final String groupId;

    public EstablishFriendshipSagaConsumer(final EstablishFriendshipSagaHandler establishFriendshipSagaHandler,
                                           @ConfigProperty(name = "mp.messaging.incoming.establish-friendship-saga.group.id") final String groupId) {
        this.establishFriendshipSagaHandler = Objects.requireNonNull(establishFriendshipSagaHandler);
        this.groupId = Objects.requireNonNull(groupId);
    }

    @Incoming("establish-friendship-saga")
    public CompletionStage<Void> onMessage(final IncomingKafkaRecord<UUID, JsonObject> message) {
        return CompletableFuture.runAsync(() -> {
            try {
                final String eventType = getHeaderAsString(message, "eventType");
                final String aggregateId = getHeaderAsString(message, "aggregateId");
                final UUID eventId = message.getKey();

                establishFriendshipSagaHandler.onEvent(groupId,
                        eventId,
                        eventType,
                        aggregateId,
                        message.getPayload(),
                        message.getTimestamp());
            } catch (Exception e) {
                LOG.error("Error while consuming friends event", e.getCause());
                throw e;
            }
        }).thenRun(() -> message.ack());
    }

}
