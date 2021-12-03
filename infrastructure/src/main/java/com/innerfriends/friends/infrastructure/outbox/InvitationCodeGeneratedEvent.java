package com.innerfriends.friends.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.InvitationCodeGenerated;
import com.innerfriends.friends.infrastructure.InstantProvider;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public final class InvitationCodeGeneratedEvent implements FriendExportedEvent {

    public static final String TYPE = "InvitationCodeGenerated";

    private final FromFriendId fromFriendId;
    private final JsonNode invitationCodeGenerated;
    private final Instant timestamp;

    private InvitationCodeGeneratedEvent(final FromFriendId fromFriendId,
                                         final JsonNode invitationCodeGenerated,
                                         final Instant timestamp) {
        this.fromFriendId = Objects.requireNonNull(fromFriendId);
        this.invitationCodeGenerated = Objects.requireNonNull(invitationCodeGenerated);
        this.timestamp = Objects.requireNonNull(timestamp);
    }

    public static InvitationCodeGeneratedEvent of(final InvitationCodeGenerated invitationCodeGenerated,
                                                  final ObjectMapper objectMapper,
                                                  final InstantProvider instantProvider) {
        final ObjectNode asJson = objectMapper.createObjectNode()
                .put("fromFriendId", invitationCodeGenerated.fromFriendId().pseudo())
                .put("invitationCode", invitationCodeGenerated.invitationCode().code().toString())
                .put("generatedAt", invitationCodeGenerated.generatedAt().at().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return new InvitationCodeGeneratedEvent(invitationCodeGenerated.fromFriendId(), asJson, instantProvider.now());
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
        return invitationCodeGenerated;
    }

}
