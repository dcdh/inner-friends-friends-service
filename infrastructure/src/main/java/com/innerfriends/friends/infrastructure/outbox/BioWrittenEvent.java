package com.innerfriends.friends.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.infrastructure.InstantProvider;

import java.time.Instant;
import java.util.Objects;

public final class BioWrittenEvent implements FriendExportedEvent {

    public static final String TYPE = "BioWritten";

    private final FriendId friendId;
    private final JsonNode bioWritten;
    private final Instant timestamp;

    private BioWrittenEvent(final FriendId friendId, final JsonNode bioWritten, final Instant timestamp) {
        this.friendId = Objects.requireNonNull(friendId);
        this.bioWritten = Objects.requireNonNull(bioWritten);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    public static BioWrittenEvent of(final Friend friendWithBioWritten,
                                     final ObjectMapper objectMapper,
                                     final InstantProvider instantProvider) {
        final ObjectNode asJson = objectMapper.createObjectNode()
                .put("friendId", friendWithBioWritten.friendId().pseudo())
                .put("bio", friendWithBioWritten.bio().content())
                .put("version", friendWithBioWritten.version().value());
        return new BioWrittenEvent(friendWithBioWritten.friendId(), asJson, instantProvider.now());
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
        return bioWritten;
    }

}
