package com.innerfriends.friends.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.infrastructure.InstantProvider;

import java.time.Instant;
import java.util.Objects;

public final class ToFriendEstablishedAFriendshipWithFromFriendEvent implements FriendExportedEvent {

    public static final String TYPE = "ToFriendEstablishedAFriendshipWithFromFriend";

    private final FriendId toFriendId;
    private final JsonNode aFriendshipEstablishedToFriendWithFromFriend;
    private final Instant timestamp;

    private ToFriendEstablishedAFriendshipWithFromFriendEvent(final FriendId toFriendId,
                                                              final JsonNode aFriendshipEstablishedToFriendWithFromFriend,
                                                              final Instant timestamp) {
        this.toFriendId = Objects.requireNonNull(toFriendId);
        this.aFriendshipEstablishedToFriendWithFromFriend = Objects.requireNonNull(aFriendshipEstablishedToFriendWithFromFriend);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    public static ToFriendEstablishedAFriendshipWithFromFriendEvent of(final Friend friendConnected,
                                                                       final ObjectMapper objectMapper,
                                                                       final InstantProvider instantProvider) {
        final ObjectNode asJson = objectMapper.createObjectNode()
                .put("friendId", friendConnected.friendId().pseudo())
                .put("establishedFriendshipWith", friendConnected.lastEstablishedFriendshipWith().pseudo())
                .put("version", friendConnected.version().value());
        return new ToFriendEstablishedAFriendshipWithFromFriendEvent(friendConnected.friendId(), asJson, instantProvider.now());
    }

    @Override
    public String getAggregateId() {
        return toFriendId.pseudo();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public JsonNode getPayload() {
        return aFriendshipEstablishedToFriendWithFromFriend;
    }

}
