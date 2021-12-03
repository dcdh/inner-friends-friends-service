package com.innerfriends.friends.infrastructure.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import io.debezium.outbox.quarkus.ExportedEvent;

public interface FriendExportedEvent extends ExportedEvent<String, JsonNode> {

    @Override
    default String getAggregateType() {
        return "Friend";
    }

}
