package com.innerfriends.friends.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.infrastructure.InstantProvider;

import java.time.Instant;
import java.util.Objects;

public final class FromFriendEstablishedAFriendshipWithToFriendEvent implements FriendExportedEvent {

    public static final String TYPE = "FromFriendEstablishedAFriendshipWithToFriend";

    private final FriendId fromFriendId;
    private final JsonNode aFriendshipEstablishedFromFriendWithToFriend;
    private final Instant timestamp;

    private FromFriendEstablishedAFriendshipWithToFriendEvent(final FriendId fromFriendId,
                                                              final JsonNode aFriendshipEstablishedFromFriendWithToFriend,
                                                              final Instant timestamp) {
        this.fromFriendId = Objects.requireNonNull(fromFriendId);
        this.aFriendshipEstablishedFromFriendWithToFriend = Objects.requireNonNull(aFriendshipEstablishedFromFriendWithToFriend);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    public static FromFriendEstablishedAFriendshipWithToFriendEvent of(final Friend friendConnected,
                                                                       final ObjectMapper objectMapper,
                                                                       final InstantProvider instantProvider) {
        final ObjectNode asJson = objectMapper.createObjectNode()
                .put("friendId", friendConnected.friendId().pseudo())
                .put("establishedFriendshipWith", friendConnected.lastEstablishedFriendshipWith().pseudo())
                .put("version", friendConnected.version().value());
        return new FromFriendEstablishedAFriendshipWithToFriendEvent(friendConnected.friendId(), asJson, instantProvider.now());
    }

    @Override
    public String getAggregateId() {
        return fromFriendId.pseudo();
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
        return aFriendshipEstablishedFromFriendWithToFriend;
    }

}
