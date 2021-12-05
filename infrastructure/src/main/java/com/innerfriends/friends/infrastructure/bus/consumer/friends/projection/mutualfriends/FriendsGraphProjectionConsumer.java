package com.innerfriends.friends.infrastructure.bus.consumer.friends.projection.mutualfriends;

import com.innerfriends.friends.infrastructure.bus.consumer.KafkaEventConsumer;
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
public class FriendsGraphProjectionConsumer extends KafkaEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(FriendsGraphProjectionConsumer.class);

    private static final String EVENT_TYPE = "eventType";
    private static final String AGGREGATE_ID = "aggregateId";

    private final FriendsGraphProjectionHandler friendsGraphProjectionHandler;
    private final String groupId;

    public FriendsGraphProjectionConsumer(final FriendsGraphProjectionHandler friendsGraphProjectionHandler,
                                          @ConfigProperty(name = "mp.messaging.incoming.friends-graph-projection.group.id") final String groupId) {
        this.friendsGraphProjectionHandler = Objects.requireNonNull(friendsGraphProjectionHandler);
        this.groupId = Objects.requireNonNull(groupId);
    }

    @Incoming("friends-graph-projection")
    public CompletionStage<Void> onMessage(final IncomingKafkaRecord<UUID, JsonObject> message) {
        return CompletableFuture.runAsync(() -> {
            try {
                final String eventType = getHeaderAsString(message, EVENT_TYPE);
                final String aggregateId = getHeaderAsString(message, AGGREGATE_ID);
                final UUID eventId = message.getKey();

                friendsGraphProjectionHandler.onEvent(groupId,
                        eventId,
                        eventType,
                        aggregateId,
                        message.getPayload(),
                        message.getTimestamp());
            } catch (final Exception exception) {
                LOG.error("Error while consuming message", exception.getCause());
                throw exception;
            }
        }).thenRun(() -> message.ack());
    }


}
