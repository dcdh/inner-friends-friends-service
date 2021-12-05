package com.innerfriends.friends.infrastructure.bus.consumer.keycloak;

import io.smallrye.reactive.messaging.kafka.IncomingKafkaRecord;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KeycloakUserAttributeConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserAttributeConsumer.class);

    private final KeycloakUserAttributeCreatedHandler keycloakUserAttributeCreatedHandler;

    public KeycloakUserAttributeConsumer(final KeycloakUserAttributeCreatedHandler keycloakUserAttributeCreatedHandler) {
        this.keycloakUserAttributeCreatedHandler = Objects.requireNonNull(keycloakUserAttributeCreatedHandler);
    }

    @Incoming("keycloak-user-attribute")
    public CompletionStage<Void> onMessage(final IncomingKafkaRecord<JsonObject, JsonObject> message) {
        // je dois tester si deja présent avant d'appliquer l'event !
        return CompletableFuture.runAsync(() -> {
            try {
                final JsonObject after = message.getPayload().getJsonObject("after");
                final String name = after.getString("name");
                final String value = after.getString("value");
                keycloakUserAttributeCreatedHandler.onUserAttributeCreated(name, value);
            } catch (Exception e) {
                LOG.error("Error while consuming keycloak user attribute event", e.getCause());
                throw e;
            }
        }).thenRun(() -> message.ack());
    }


}
