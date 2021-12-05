package com.innerfriends.friends.infrastructure.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class RedpandaTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private final Logger logger = LoggerFactory.getLogger(RedpandaTestResourceLifecycleManager.class);

    private PostgreSQLContainer<?> postgresMutableContainer;

    private PostgreSQLContainer<?> postgresKeycloakContainer;

    private GenericContainer<?> keycloakContainer;

    private GenericContainer<?> redpandaContainer;

    private GenericContainer<?> debeziumConnectContainer;

    private GenericContainer<?> kafkaUiContainer;

    private Network network;

    @Override
    public Map<String, String> start() {
        final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
        network = Network.newNetwork();
        postgresMutableContainer = new PostgreSQLContainer<>(
                DockerImageName.parse("debezium/postgres:11-alpine")
                        .asCompatibleSubstituteFor("postgres"))
                .withNetwork(network)
                .withNetworkAliases("mutable")
                .withDatabaseName("mutable")
                .withUsername("postgresql")
                .withPassword("postgresql")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1));
        postgresMutableContainer.start();
        postgresMutableContainer.followOutput(logConsumer);
        postgresKeycloakContainer = new PostgreSQLContainer<>(
                DockerImageName.parse("debezium/postgres:11-alpine")
                        .asCompatibleSubstituteFor("postgres"))
                .withNetwork(network)
                .withNetworkAliases("keycloak-db")
                .withDatabaseName("keycloak")
                .withUsername("keycloak")
                .withPassword("keycloak")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1));
        postgresKeycloakContainer.start();
        postgresKeycloakContainer.followOutput(logConsumer);
        keycloakContainer = new GenericContainer<>("damdamdeo/inner-friends-keycloack")
                .withNetwork(network)
                .withExposedPorts(8080)
                .withEnv("KEYCLOAK_USER", "keycloak")
                .withEnv("KEYCLOAK_PASSWORD", "keycloak")
                .withEnv("DB_VENDOR", "postgres")
                .withEnv("DB_ADDR", "keycloak-db:5432")
                .withEnv("DB_DATABASE", "keycloak")
                .withEnv("DB_USER", "keycloak")
                .withEnv("DB_PASSWORD", "keycloak")
                .waitingFor(Wait.forLogMessage(".* Admin console listening on.*", 1));
        keycloakContainer.start();
        keycloakContainer.followOutput(logConsumer);
        redpandaContainer = new RedPandaKafkaContainer(network);
        redpandaContainer.start();
        redpandaContainer.followOutput(logConsumer);
        debeziumConnectContainer = new GenericContainer<>("debezium/connect:1.8")
                .withNetwork(network)
                .withExposedPorts(8083)
                .withEnv("KAFKA_LOG4J_OPTS", "-Dlog4j.configuration=file:/opt/kafka/config/connect-log4j.properties")
                .withEnv("BOOTSTRAP_SERVERS", "redpanda:9092")
                .withEnv("KEY_CONVERTER", "org.apache.kafka.connect.json.JsonConverter")
                .withEnv("VALUE_CONVERTER", "org.apache.kafka.connect.json.JsonConverter")
                .withEnv("GROUP_ID", "1")
                .withEnv("CONFIG_STORAGE_TOPIC", "my_connect_configs")
                .withEnv("OFFSET_STORAGE_TOPIC", "my_connect_offsets")
                .withEnv("STATUS_STORAGE_TOPIC", "my_connect_statuses")
                .waitingFor(Wait.forLogMessage(".*Finished starting connectors and tasks.*", 1));
        debeziumConnectContainer.start();
        debeziumConnectContainer.followOutput(logConsumer);
        kafkaUiContainer = new GenericContainer<>("provectuslabs/kafka-ui:0.2.1")
                .withNetwork(network)
                .withExposedPorts(8080)
                .withEnv("KAFKA_CLUSTERS_0_NAME", "local")
                .withEnv("KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS", "redpanda:9092");
        kafkaUiContainer.start();

        // Start connector on Keycloak
        final String connectorSetup = "{\n" +
                "  \"name\": \"keycloak-connector\",\n" +
                "  \"config\": {\n" +
                "    \"connector.class\": \"io.debezium.connector.postgresql.PostgresConnector\",\n" +
                "    \"database.hostname\": \"keycloak-db\",\n" +
                "    \"database.port\": \"5432\",\n" +
                "    \"slot.name\": \"keycloak_debezium\",\n" +
                "    \"plugin.name\": \"pgoutput\",\n" +
                "    \"database.user\": \"keycloak\",\n" +
                "    \"database.password\": \"keycloak\",\n" +
                "    \"database.dbname\": \"keycloak\",\n" +
                "    \"database.server.name\": \"keycloak-db\",\n" +
                "    \"schema.include.list\": \"public\",\n" +
                "    \"table.include.list\": \"public.user_attribute\",\n" +
                "    \"topic.creation.default.replication.factor\": -1,\n" +
                "    \"topic.creation.default.partitions\": 1,\n" +
                "    \"topic.creation.default.cleanup.policy\": \"compact\",\n" +
                "    \"key.converter\": \"org.apache.kafka.connect.json.JsonConverter\",\n" +
                "    \"key.converter.schemas.enable\": \"false\",\n" +
                "    \"value.converter\": \"org.apache.kafka.connect.json.JsonConverter\",\n" +
                "    \"value.converter.schemas.enable\": \"false\"\n" +
                "  }\n" +
                "}";
        given()
                .body(connectorSetup)
                .contentType(ContentType.JSON)
                .post(URI.create(String.format("http://localhost:%d/connectors/", debeziumConnectContainer.getMappedPort(8083))))
                .then()
                .log().all()
                .statusCode(201);

        return new HashMap<>() {{
            put("quarkus.datasource.jdbc.url", postgresMutableContainer.getJdbcUrl());
            put("quarkus.datasource.username", postgresMutableContainer.getUsername());
            put("quarkus.datasource.password", postgresMutableContainer.getPassword());
            put("kafka-connector-api/mp-rest/url",
                    String.format("http://%s:%d", "localhost", debeziumConnectContainer.getMappedPort(8083)));
            put("connector.mutable.database.hostname", "mutable");
            put("connector.mutable.database.username", postgresMutableContainer.getUsername());
            put("connector.mutable.database.password", postgresMutableContainer.getPassword());
            put("connector.mutable.database.port", "5432");
            put("connector.mutable.database.dbname", postgresMutableContainer.getDatabaseName());
            put("slot.drop.on.stop", "true");
            put("snapshot.mode", "always");
            put("connector.mutable.enabled", "true");
            put("kafka.exposed.port.9092", redpandaContainer.getMappedPort(9092).toString());

            // friends-graph-projection
            put("mp.messaging.incoming.friends-graph-projection.connector", "smallrye-kafka");
            put("mp.messaging.incoming.friends-graph-projection.topic", "Friend.events");
            put("mp.messaging.incoming.friends-graph-projection.group.id", "mutual-friends-graph-projection");
            put("mp.messaging.incoming.friends-graph-projection.key.deserializer", "org.apache.kafka.common.serialization.UUIDDeserializer");
            put("mp.messaging.incoming.friends-graph-projection.value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.friends-graph-projection.bootstrap.servers",
                    String.format("%s:%d", "localhost", redpandaContainer.getMappedPort(9092)));
            put("mp.messaging.incoming.friends-graph-projection.auto.offset.reset", "earliest");
            // establish-friendship-saga
            put("mp.messaging.incoming.establish-friendship-saga.connector", "smallrye-kafka");
            put("mp.messaging.incoming.establish-friendship-saga.topic", "Friend.events");
            put("mp.messaging.incoming.establish-friendship-saga.group.id", "establish-friendship-saga");
            put("mp.messaging.incoming.establish-friendship-saga.key.deserializer", "org.apache.kafka.common.serialization.UUIDDeserializer");
            put("mp.messaging.incoming.establish-friendship-saga.value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.establish-friendship-saga.bootstrap.servers",
                    String.format("%s:%s", "localhost", redpandaContainer.getMappedPort(9092)));
            put("mp.messaging.incoming.establish-friendship-saga.auto.offset.reset", "earliest");
            // keycloak-user-attribute
            put("mp.messaging.incoming.keycloak-user-attribute.connector", "smallrye-kafka");
            put("mp.messaging.incoming.keycloak-user-attribute.topic", "keycloak-db.public.user_attribute");
            put("mp.messaging.incoming.keycloak-user-attribute.group.id", "keycloak-user-attribute");
            put("mp.messaging.incoming.keycloak-user-attribute.key.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.keycloak-user-attribute.value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.keycloak-user-attribute.bootstrap.servers",
                    String.format("%s:%s", "localhost", redpandaContainer.getMappedPort(9092)));
            put("mp.messaging.incoming.keycloak-user-attribute.auto.offset.reset", "earliest");
            // keycloak
            put("quarkus.oidc.auth-server-url", String.format("http://localhost:%d/auth/realms/public", keycloakContainer.getMappedPort(8080)));
            put("keycloak.admin.adminRealm", "master");
            put("keycloak.admin.clientId", "admin-cli");
            put("keycloak.admin.username", keycloakContainer.getEnvMap().get("KEYCLOAK_USER"));
            put("keycloak.admin.password", keycloakContainer.getEnvMap().get("KEYCLOAK_PASSWORD"));
        }};
    }

    @Override
    public void stop() {
        if (postgresMutableContainer != null) {
            postgresMutableContainer.close();
        }
        if (postgresKeycloakContainer != null) {
            postgresKeycloakContainer.close();
        }
        if (keycloakContainer != null) {
            keycloakContainer.close();
        }
        if (redpandaContainer != null) {
            redpandaContainer.close();
        }
        if (debeziumConnectContainer != null) {
            debeziumConnectContainer.close();
        }
        if (network != null) {
            network.close();
        }
    }

}