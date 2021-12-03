package com.innerfriends.friends.infrastructure.resources;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

public class RedpandaTestResourceLifecycleManager implements QuarkusTestResourceLifecycleManager {

    private final Logger logger = LoggerFactory.getLogger(RedpandaTestResourceLifecycleManager.class);

    private PostgreSQLContainer<?> postgresContainer;

    private GenericContainer<?> redpandaContainer;

    private GenericContainer<?> debeziumConnectContainer;

    private GenericContainer<?> kafkaUiContainer;

    private Network network;

    @Override
    public Map<String, String> start() {
        final Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(logger);
        network = Network.newNetwork();
        postgresContainer = new PostgreSQLContainer<>(
                DockerImageName.parse("debezium/postgres:11-alpine")
                        .asCompatibleSubstituteFor("postgres"))
                .withNetwork(network)
                .withNetworkAliases("mutable")
                .withDatabaseName("mutable")
                .withUsername("postgresql")
                .withPassword("postgresql")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*", 1));
        postgresContainer.start();
        postgresContainer.followOutput(logConsumer);
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

        return new HashMap<>() {{
            put("quarkus.datasource.jdbc.url", postgresContainer.getJdbcUrl());
            put("quarkus.datasource.username", postgresContainer.getUsername());
            put("quarkus.datasource.password", postgresContainer.getPassword());
            put("kafka-connector-api/mp-rest/url",
                    String.format("http://%s:%d", "localhost", debeziumConnectContainer.getMappedPort(8083)));
            put("connector.mutable.database.hostname", "mutable");
            put("connector.mutable.database.username", postgresContainer.getUsername());
            put("connector.mutable.database.password", postgresContainer.getPassword());
            put("connector.mutable.database.port", "5432");
            put("connector.mutable.database.dbname", postgresContainer.getDatabaseName());
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
        }};
    }

    @Override
    public void stop() {
        if (postgresContainer != null) {
            postgresContainer.close();
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