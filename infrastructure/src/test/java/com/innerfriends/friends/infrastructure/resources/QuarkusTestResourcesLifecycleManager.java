package com.innerfriends.friends.infrastructure.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.restassured.http.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class QuarkusTestResourcesLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuarkusTestResourcesLifecycleManager.class);

    private static final Network NETWORK = Network.newNetwork();

    private static final GenericContainer<?> ARANGO_DB_CONTAINER = new GenericContainer<>("arangodb:3.8.3")
            .withExposedPorts(8529)
            .withNetwork(NETWORK)
            .withNetworkAliases("arangodb")
            .withEnv("ARANGO_ROOT_PASSWORD", "password")
            .withEnv("ARANGODB_OVERRIDE_DETECTED_TOTAL_MEMORY", "100m")
            .waitingFor(Wait.forLogMessage(".*is ready for business.*", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    private static final GenericContainer<?> HAZELCAST_CONTAINER = new GenericContainer("hazelcast/hazelcast:4.1.5")
            .withExposedPorts(5701)
            .withNetwork(NETWORK)
            .waitingFor(Wait.forLogMessage(".*is STARTED.*\\n", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    private static final PostgreSQLContainer<?> POSTGRES_MUTABLE_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse("debezium/postgres:11-alpine")
                    .asCompatibleSubstituteFor("postgres"))
            .withNetwork(NETWORK)
            .withNetworkAliases("mutable")
            .withDatabaseName("mutable")
            .withUsername("postgresql")
            .withPassword("postgresql")
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    private static final PostgreSQLContainer<?> POSTGRES_KEYCLOAK_CONTAINER = new PostgreSQLContainer<>(
            DockerImageName.parse("debezium/postgres:11-alpine")
                    .asCompatibleSubstituteFor("postgres")
            .asCompatibleSubstituteFor("postgres"))
            .withNetwork(NETWORK)
            .withNetworkAliases("keycloak-db")
            .withDatabaseName("keycloak")
            .withUsername("keycloak")
            .withPassword("keycloak")
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    private static final GenericContainer<?> KEYCLOAK_CONTAINER = new GenericContainer<>("damdamdeo/inner-friends-keycloack")
            .withNetwork(NETWORK)
            .withExposedPorts(8080)
            .withEnv("KEYCLOAK_USER", "keycloak")
            .withEnv("KEYCLOAK_PASSWORD", "keycloak")
            .withEnv("DB_VENDOR", "postgres")
            .withEnv("DB_ADDR", "keycloak-db:5432")
            .withEnv("DB_DATABASE", "keycloak")
            .withEnv("DB_USER", "keycloak")
            .withEnv("DB_PASSWORD", "keycloak")
            .waitingFor(Wait.forLogMessage(".* Admin console listening on.*", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    private static final GenericContainer<?> RED_PANDA_CONTAINER = new RedPandaKafkaContainer(NETWORK);

    private static final GenericContainer<?> DEBEZIUM_CONNECT_CONTAINER = new GenericContainer<>("debezium/connect:1.8")
            .withNetwork(NETWORK)
            .withNetworkAliases("connect")
            .withExposedPorts(8083)
            .withEnv("KAFKA_LOG4J_OPTS", "-Dlog4j.configuration=file:/opt/kafka/config/connect-log4j.properties")
            .withEnv("BOOTSTRAP_SERVERS", "redpanda:9092")
            .withEnv("KEY_CONVERTER", "org.apache.kafka.connect.json.JsonConverter")
            .withEnv("VALUE_CONVERTER", "org.apache.kafka.connect.json.JsonConverter")
            .withEnv("GROUP_ID", "1")
            .withEnv("CONFIG_STORAGE_TOPIC", "my_connect_configs")
            .withEnv("OFFSET_STORAGE_TOPIC", "my_connect_offsets")
            .withEnv("STATUS_STORAGE_TOPIC", "my_connect_statuses")
            .waitingFor(Wait.forLogMessage(".*Finished starting connectors and tasks.*", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    private static final GenericContainer<?> JAEGER_TRACING_ALL_IN_ONE_CONTAINER = new GenericContainer("jaegertracing/all-in-one:1.25.0")
            .withExposedPorts(16686, 14268, 14250)
            .withNetwork(NETWORK)
            .withNetworkAliases("jaeger-all-in-one")
            .waitingFor(Wait.forLogMessage(".*Channel Connectivity change to READY.*\\n", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    private static final GenericContainer<?> OTEL_OPENTELEMETRY_COLLECTOR_CONTAINER = new GenericContainer("otel/opentelemetry-collector:0.33.0")
            .withExposedPorts(13133, 4317, 55680)
            .withNetwork(NETWORK)
            .withNetworkAliases("otel-collector")
            .withCommand("--config=/etc/otel-collector-config.yaml")
            .withClasspathResourceMapping("/given/otel-collector-config.yaml", "/etc/otel-collector-config.yaml", BindMode.READ_ONLY)
            .waitingFor(Wait.forLogMessage(".*Everything is ready. Begin running and processing data.*\\n", 1))
            .withLogConsumer(new Slf4jLogConsumer(LOGGER));

    @Override
    public Map<String, String> start() {
        Stream.of(ARANGO_DB_CONTAINER, HAZELCAST_CONTAINER, POSTGRES_MUTABLE_CONTAINER, POSTGRES_KEYCLOAK_CONTAINER,
                KEYCLOAK_CONTAINER, RED_PANDA_CONTAINER,DEBEZIUM_CONNECT_CONTAINER, JAEGER_TRACING_ALL_IN_ONE_CONTAINER,
                OTEL_OPENTELEMETRY_COLLECTOR_CONTAINER)
                .forEach(genericContainer -> genericContainer.start());
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
                .post(URI.create(String.format("http://localhost:%d/connectors/", DEBEZIUM_CONNECT_CONTAINER.getMappedPort(8083))))
                .then()
                .log().all()
                .statusCode(201);

        return new HashMap<>() {{
            // arangodb
            put("arangodb.host", "localhost");
            put("arangodb.port", ARANGO_DB_CONTAINER.getMappedPort(8529).toString());
            put("arangodb.user", "root");
            put("arangodb.password", "password");
            put("arangodb.dbName", "friends");
            // hazelcast
            put("quarkus.hazelcast-client.cluster-name", "dev");
            put("quarkus.hazelcast-client.cluster-members", String.format("localhost:%d", HAZELCAST_CONTAINER.getMappedPort(5701)));
            // datasource
            put("quarkus.datasource.jdbc.url", POSTGRES_MUTABLE_CONTAINER.getJdbcUrl());
            put("quarkus.datasource.username", POSTGRES_MUTABLE_CONTAINER.getUsername());
            put("quarkus.datasource.password", POSTGRES_MUTABLE_CONTAINER.getPassword());
            // connector
            put("kafka-connector-api/mp-rest/url",
                    String.format("http://%s:%d", "localhost", DEBEZIUM_CONNECT_CONTAINER.getMappedPort(8083)));
            put("connector.mutable.database.hostname", "mutable");
            put("connector.mutable.database.username", POSTGRES_MUTABLE_CONTAINER.getUsername());
            put("connector.mutable.database.password", POSTGRES_MUTABLE_CONTAINER.getPassword());
            put("connector.mutable.database.port", "5432");
            put("connector.mutable.database.dbname", POSTGRES_MUTABLE_CONTAINER.getDatabaseName());
            put("slot.drop.on.stop", "true");
            put("snapshot.mode", "always");
            put("connector.mutable.enabled", "true");
            put("kafka.exposed.port.9092", RED_PANDA_CONTAINER.getMappedPort(9092).toString());
            // friends-graph-projection
            put("mp.messaging.incoming.friends-graph-projection.connector", "smallrye-kafka");
            put("mp.messaging.incoming.friends-graph-projection.topic", "Friend.events");
            put("mp.messaging.incoming.friends-graph-projection.group.id", "mutual-friends-graph-projection");
            put("mp.messaging.incoming.friends-graph-projection.key.deserializer", "org.apache.kafka.common.serialization.UUIDDeserializer");
            put("mp.messaging.incoming.friends-graph-projection.value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.friends-graph-projection.bootstrap.servers",
                    String.format("%s:%d", "localhost", RED_PANDA_CONTAINER.getMappedPort(9092)));
            put("mp.messaging.incoming.friends-graph-projection.auto.offset.reset", "earliest");
            // establish-friendship-saga
            put("mp.messaging.incoming.establish-friendship-saga.connector", "smallrye-kafka");
            put("mp.messaging.incoming.establish-friendship-saga.topic", "Friend.events");
            put("mp.messaging.incoming.establish-friendship-saga.group.id", "establish-friendship-saga");
            put("mp.messaging.incoming.establish-friendship-saga.key.deserializer", "org.apache.kafka.common.serialization.UUIDDeserializer");
            put("mp.messaging.incoming.establish-friendship-saga.value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.establish-friendship-saga.bootstrap.servers",
                    String.format("%s:%s", "localhost", RED_PANDA_CONTAINER.getMappedPort(9092)));
            put("mp.messaging.incoming.establish-friendship-saga.auto.offset.reset", "earliest");
            // keycloak-user-attribute
            put("mp.messaging.incoming.keycloak-user-attribute.connector", "smallrye-kafka");
            put("mp.messaging.incoming.keycloak-user-attribute.topic", "keycloak-db.public.user_attribute");
            put("mp.messaging.incoming.keycloak-user-attribute.group.id", "keycloak-user-attribute");
            put("mp.messaging.incoming.keycloak-user-attribute.key.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.keycloak-user-attribute.value.deserializer", "io.vertx.kafka.client.serialization.JsonObjectDeserializer");
            put("mp.messaging.incoming.keycloak-user-attribute.bootstrap.servers",
                    String.format("%s:%s", "localhost", RED_PANDA_CONTAINER.getMappedPort(9092)));
            put("mp.messaging.incoming.keycloak-user-attribute.auto.offset.reset", "earliest");
            // keycloak
            put("quarkus.oidc.auth-server-url", String.format("http://localhost:%d/auth/realms/public", KEYCLOAK_CONTAINER.getMappedPort(8080)));
            put("keycloak.admin.adminRealm", "master");
            put("keycloak.admin.clientId", "admin-cli");
            put("keycloak.admin.username", KEYCLOAK_CONTAINER.getEnvMap().get("KEYCLOAK_USER"));
            put("keycloak.admin.password", KEYCLOAK_CONTAINER.getEnvMap().get("KEYCLOAK_PASSWORD"));
            // jaeger
            put("jaeger.exposed.port.16686", JAEGER_TRACING_ALL_IN_ONE_CONTAINER.getMappedPort(16686).toString());
            // opentelemtry
            put("quarkus.opentelemetry.tracer.exporter.otlp.endpoint", String.format("http://localhost:%d", OTEL_OPENTELEMETRY_COLLECTOR_CONTAINER.getMappedPort(55680)));
        }};
    }

    @Override
    public void stop() {

    }
}
