## What

Friend domain.

## Prerequisites

`docker` is mandatory to be able to build the project. The infrastructure relie on `testcontainers` to start containers before starting tests and kill and remove them after running the tests.

## Docker

Stop and remove all containers `docker stop $(docker ps -a -q); docker rm $(docker ps -a -q)`

## Dev local env
1. run `docker-compose -f docker-compose-dev-run.yaml up && docker-compose -f docker-compose-dev-run.yaml rm --force` to start the stack and next remove container after
1. run `mvn compile quarkus:dev -f infrastructure/pom.xml`
1. access swagger ui via `http://0.0.0.0:8080/q/swagger-ui/`
1. access jaeger ui via `http://localhost:16686/`
1. access kafka ui via `http://localhost:8081/`

## How to build and run

1. run to prepare application containers `docker pull quay.io/quarkus/ubi-quarkus-native-image:21.3-java11 && docker pull arangodb:3.8.3 && docker pull hazelcast/hazelcast:4.1.5 && docker pull debezium/postgres:11-alpine && docker pull debezium/kafka:1.8 && docker pull debezium/connect:1.8 && docker pull provectuslabs/kafka-ui:0.2.1 && docker pull jaegertracing/all-in-one:1.25.0 && docker pull otel/opentelemetry-collector:0.33.0 && docker pull vectorized/redpanda:v21.9.6`
1. run `mvn clean install verify -P native -Dquarkus.native.container-build=true` to build everything;
1. run `docker build -f infrastructure/src/main/docker/Dockerfile.native-distroless -t damdamdeo/inner-friends-friends-service infrastructure/` to build docker image
1. run `docker-compose -f docker-compose-local-run.yaml up && docker-compose -f docker-compose-local-run.yaml rm --force` to start the stack and next remove container after
1. access swagger ui via `http://0.0.0.0:8080/q/swagger-ui/`
1. access jaeger ui via `http://localhost:16686/`
1. access kafka ui via `http://localhost:8081/`
1. access arangodb ui via `http://localhost:8529/`
1. access keycloak ui via `http://localhost:8082/`

## Tips

### How to get the token

```
TOKEN="$(curl http://localhost:8082/auth/realms/public/protocol/openid-connect/token -d grant_type=password -d client_id=public -d client_secret=4d8d4bc6-1eda-433b-ab6b-967a3a4bdd95 -d username=damdamdeo -d password=InnerFriendsDamien1983 -s |jq -r .access_token)"; echo $TOKEN
```

### How to trace smallrye JWT logs

Run the container with this option
> quarkus.log.category."io.quarkus.smallrye.jwt.runtime.auth.MpJwtValidator".level=TRACE

### Fix `iss` issue when keycloak is running inside a docker container
> https://medium.com/swlh/keycloak-openshift-and-emails-a-tale-of-links-with-wrong-base-urls-15f445d4b6a1
>
> define `KEYCLOAK_FRONTEND_URL`

```
Caused by: org.jose4j.jwt.consumer.InvalidJwtException: JWT (claims->{"exp":1639947046,"iat":1639946746,"jti":"67188abb-4196-4274-a374-6368e7bfc463","iss":"http://localhost:8082/auth/realms/public","aud":"account","sub":"608105ae-5f5b-4df1-be2c-7b8ec57eb317","typ":"Bearer","azp":"public","session_state":"b3864dff-4e6e-46a4-ad56-e7c62dec9554","acr":"1","realm_access":{"roles":["offline_access","default-roles-public","uma_authorization"]},"resource_access":{"public":{"roles":["friend"]},"account":{"roles":["manage-account","manage-account-links","view-profile"]}},"scope":"profile email","sid":"b3864dff-4e6e-46a4-ad56-e7c62dec9554","email_verified":false,"friendId":"DamDamDeo","groups":["friend"],"preferred_username":"damdamdeo","email":"damdamdeo@inner-friends.com"}) rejected due to invalid claims or other invalid content. Additional details: [[12] Issuer (iss) claim value (http://localhost:8082/auth/realms/public) doesn't match expected value of http://192.168.254.184:8082/auth/realms/public]
```

## Infra

### Quarkus

#### Known issues

1. It is not possible to access the red panda in the dev mode by the quarkus instance.
TODO setup `advertise-kafka-addr` to use the container IP and not localhost or docker container network name like it is done in `RedPandaKafkaContainer`.
1. Native compilation does not work since version `2.4.0.Final`. Stuck to version `2.3.1.Final`.

###  Kafka

#### get connector status

> connect to `connect` container
> 
> curl http://localhost:8083/connectors/friends-connector/status

#### list topics

> connect to `connect` container
> 
> bin/kafka-topics.sh --list --bootstrap-server kafka:9092

#### get `ContactBook.events` topic info

> connect to `connect` container 
>
> bin/kafka-topics.sh --bootstrap-server kafka:9092 --describe --topic ContactBook.events
> 
> bin/kafka-configs.sh --bootstrap-server kafka:9092 --describe --all --topic ContactBook.events

#### read `ContactBook.events` topic messages

> connect to `connect` container
>
> bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --property print.key=true --property print.headers=true --property print.timestamp=true --topic ContactBook.events --from-beginning

#### get `Conversation.events` topic info

> connect to `connect` container 
>
> bin/kafka-topics.sh --bootstrap-server kafka:9092 --describe --topic Conversation.events
> 
> bin/kafka-configs.sh --bootstrap-server kafka:9092 --describe --all --topic Conversation.events

#### read `Conversation.events` topic messages

> connect to `connect` container
>
> bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --property print.key=true --property print.headers=true --property print.timestamp=true --topic Conversation.events --from-beginning

#### Known issues

##### Kafka unable to start image `fixed in 1.8`

- https://issues.redhat.com/browse/DBZ-4262
- https://issues.redhat.com/browse/DBZ-4160

### vectorized/redpanda

- https://hub.docker.com/r/vectorized/redpanda
- https://vectorized.io/docs/quick-start-docker/#Bring-up-a-docker-compose-file
- https://vectorized.io/blog/redpanda-debezium/#Quick-tour-on-services

Regarding running redpanda using `testcontainers` we need to inject the container ip into `--advertise-kafka-addr` however the consumer in test would not be able to communicate with redpanda.

- https://github.com/debezium/docker-images/blob/main/kafka/1.8/docker-entrypoint.sh#L67
- https://github.com/debezium/docker-images/blob/main/kafka/1.8/docker-entrypoint.sh#L99
- https://github.com/quarkusio/quarkus/blob/2.4/extensions/kafka-client/deployment/src/main/java/io/quarkus/kafka/client/deployment/DevServicesKafkaProcessor.java#L337
- https://github.com/testcontainers/testcontainers-java/issues/452#issuecomment-331184470

#### list topic

> connect to `redpanda` container
>
> rpk topic list

#### read `ContactBook.events` topic messages

> connect to `redpanda` container
>
> rpk topic consume ContactBook.events

#### read `Conversation.events` topic messages

> connect to `redpanda` container
>
> rpk topic consume Conversation.events

### Keycloak

> topic name: keycloak-db.public.user_attribute
>
> Key: "{\"id\":\"323ac6d1-860a-435c-82d1-ccd5eceec2b2\"}"
> 
> Value: "{\"before\":null,\"after\":{\"name\":\"friendId\",\"value\":\"DamDamDeo\",\"user_id\":\"9e5d1138-4505-4f3a-ac76-e74f8f27f7b4\",\"id\":\"323ac6d1-860a-435c-82d1-ccd5eceec2b2\"},\"source\":{\"version\":\"1.8.0.Alpha2\",\"connector\":\"postgresql\",\"name\":\"keycloak-db\",\"ts_ms\":1638722229490,\"snapshot\":\"last\",\"db\":\"keycloak\",\"sequence\":\"[null,\\\"28745256\\\"]\",\"schema\":\"public\",\"table\":\"user_attribute\",\"txId\":754,\"lsn\":28745256,\"xmin\":null},\"op\":\"r\",\"ts_ms\":1638722229491,\"transaction\":null}"

