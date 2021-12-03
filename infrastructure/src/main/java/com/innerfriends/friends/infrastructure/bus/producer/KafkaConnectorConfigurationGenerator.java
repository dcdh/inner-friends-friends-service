package com.innerfriends.friends.infrastructure.bus.producer;

public interface KafkaConnectorConfigurationGenerator {

    KafkaConnectorConfigurationDTO generateConnectorConfiguration(String connectorName);

}
