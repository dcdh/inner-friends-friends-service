package com.innerfriends.friends.infrastructure.bus.consumer.friends.projection.mutualfriends;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.infrastructure.InstantProvider;
import com.innerfriends.friends.infrastructure.arrangodb.ArangoDBRepositories;
import com.innerfriends.friends.infrastructure.opentelemetry.NewSpan;
import com.innerfriends.friends.infrastructure.postgres.MessageLogRepository;
import com.innerfriends.friends.infrastructure.outbox.*;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class FriendsGraphProjectionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FriendsGraphProjectionHandler.class);

    private static final String VERSION = "version";
    private static final String BIO = "bio";
    private static final String FRIEND_ID = "friendId";
    private static final String IN_FRIENDSHIPS_WITH = "inFriendshipsWith";
    private static final String ESTABLISHED_FRIENDSHIP_WITH = "establishedFriendshipWith";

    private final ArangoDBRepositories arangoDBRepositories;
    private final MessageLogRepository messageLogRepository;
    private final InstantProvider instantProvider;

    public FriendsGraphProjectionHandler(final ArangoDBRepositories arangoDBRepositories,
                                         final MessageLogRepository messageLogRepository,
                                         final InstantProvider instantProvider) {
        this.arangoDBRepositories = Objects.requireNonNull(arangoDBRepositories);
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
        if (NewFriendRegisteredIntoThePlatformEvent.TYPE.equals(eventType)) {
            final FriendId friendId = new FriendId(eventPayload.getString(FRIEND_ID));
            final List<InFriendshipWithId> inFriendshipsWith = IntStream.range(0, eventPayload.getJsonArray(IN_FRIENDSHIPS_WITH).size())
                    .mapToObj(index -> eventPayload.getJsonArray(IN_FRIENDSHIPS_WITH).getString(index))
                    .map(InFriendshipWithId::new)
                    .collect(Collectors.toList());
            final Version version = new Version(eventPayload.getLong(VERSION));
            arangoDBRepositories.registerNewFriendIntoThePlatform(friendId, inFriendshipsWith, version);
        } else if (BioWrittenEvent.TYPE.equals(eventType)) {
            final FriendId friendId = new FriendId(eventPayload.getString(FRIEND_ID));
            final Bio bio = new Bio(eventPayload.getString(BIO));
            final Version version = new Version(eventPayload.getLong(VERSION));
            arangoDBRepositories.writeBio(friendId, bio, version);
        } else if (InvitationCodeGeneratedEvent.TYPE.equals(eventType)) {
            // nothing to do
        } else if (ToFriendEstablishedAFriendshipWithFromFriendEvent.TYPE.equals(eventType)) {
            final FriendId friendId = new FriendId(eventPayload.getString(FRIEND_ID));
            final EstablishedFriendshipWith establishedFriendshipWith = new EstablishedFriendshipWith(new InFriendshipWithId(eventPayload.getString(ESTABLISHED_FRIENDSHIP_WITH)));
            final Version version = new Version(eventPayload.getLong(VERSION));
            arangoDBRepositories.establishFriendshipWith(friendId, establishedFriendshipWith, version);
        } else if (FromFriendEstablishedAFriendshipWithToFriendEvent.TYPE.equals(eventType)) {
            final FriendId friendId = new FriendId(eventPayload.getString(FRIEND_ID));
            final EstablishedFriendshipWith establishedFriendshipWith = new EstablishedFriendshipWith(new InFriendshipWithId(eventPayload.getString(ESTABLISHED_FRIENDSHIP_WITH)));
            final Version version = new Version(eventPayload.getLong(VERSION));
            arangoDBRepositories.establishFriendshipWith(friendId, establishedFriendshipWith, version);
        } else {
            LOG.info("Unknown event type '{}' for event id '{}', will be marked as processed.", eventType, eventId);
        }

        messageLogRepository.markAsConsumed(groupId, eventId, instantProvider.now());
    }


}
