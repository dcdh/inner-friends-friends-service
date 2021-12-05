package com.innerfriends.friends.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import com.innerfriends.friends.infrastructure.InstantProvider;

import java.time.Instant;
import java.util.Objects;

public final class NewFriendRegisteredIntoThePlatformEvent implements FriendExportedEvent {

    public static final String TYPE = "NewFriendRegisteredIntoThePlatform";

    private final FriendId friendId;
    private final JsonNode newFriendRegisteredIntoThePlatform;
    private final Instant timestamp;

    private NewFriendRegisteredIntoThePlatformEvent(final FriendId friendId,
                                                    final JsonNode newFriendRegisteredIntoThePlatform,
                                                    final Instant timestamp) {
        this.friendId = Objects.requireNonNull(friendId);
        this.newFriendRegisteredIntoThePlatform = Objects.requireNonNull(newFriendRegisteredIntoThePlatform);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    public static NewFriendRegisteredIntoThePlatformEvent of(final Friend newFriendRegistered,
                                                             final ObjectMapper objectMapper,
                                                             final InstantProvider instantProvider) {
        final ObjectNode asJson = objectMapper.createObjectNode()
                .put("friendId", newFriendRegistered.friendId().pseudo())
                .put("version", newFriendRegistered.version().value());
        final ArrayNode inFriendshipsWith = asJson.putArray("inFriendshipsWith");
        newFriendRegistered.inFriendshipsWith()
                .stream()
                .map(InFriendshipWithId::pseudo)
                .forEach(pseudo -> inFriendshipsWith.add(pseudo));
        return new NewFriendRegisteredIntoThePlatformEvent(newFriendRegistered.friendId(), asJson, instantProvider.now());
    }

    @Override
    public String getAggregateId() {
        return friendId.pseudo();
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
        return newFriendRegisteredIntoThePlatform;
    }

}
