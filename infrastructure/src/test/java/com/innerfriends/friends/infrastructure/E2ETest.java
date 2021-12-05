package com.innerfriends.friends.infrastructure;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.infrastructure.bus.producer.KafkaConnectorApi;
import com.innerfriends.friends.infrastructure.bus.producer.OutboxConnectorStarter;
import com.innerfriends.friends.infrastructure.postgres.FriendEntity;
import com.innerfriends.friends.infrastructure.postgres.InvitationCodeGeneratedEntity;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class E2ETest {

    @Inject
    OutboxConnectorStarter outboxConnectorStarter;

    @Inject
    @RestClient
    KafkaConnectorApi kafkaConnectorApi;

    @Inject
    UserTransaction userTransaction;

    @Inject
    EntityManager entityManager;

    @Inject
    TracesUtils tracesUtils;

    @Inject
    TopicConsumer topicConsumer;

    @Inject
    ArangoDBFriends arangoDBFriends;

    @Inject
    KeycloakAdminClient keycloakAdminClient;

    @BeforeEach
    public void setup() {
        outboxConnectorStarter.start();
        Awaitility.await()
                .atMost(Durations.FIVE_SECONDS)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
                .until(() ->
                        "RUNNING".equals(kafkaConnectorApi.connectorStatus("outbox-connector").connector.state));
    }

    @Test
    @Order(1)
    public void should_have_DamDamDeo_registered() throws Exception {
        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
                .until(() -> runInTransaction(() -> entityManager.find(FriendEntity.class, "DamDamDeo") != null));

        final Friend expectedDamDamDeo = new Friend(new FriendId("DamDamDeo"));
        final FriendEntity damDamDeoEntity = runInTransaction(() -> entityManager.find(FriendEntity.class, "DamDamDeo"));
        assertThat(damDamDeoEntity.toFriend()).isEqualTo(expectedDamDamDeo);

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(1);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("ANewFriendRegisteredIntoThePlatform".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("DamDamDeo".getBytes());

        assertThat(arangoDBFriends.getFriend("DamDamDeo")).isEqualTo(expectedDamDamDeo);
    }

    @Test
    @Order(2)
    public void should_register_mario_as_a_new_friend_into_the_platform() throws Exception {
        // TODO replace by a message consumed from kafka
        // Given

        // When
        keycloakAdminClient.register("Mario");
        waitForFriendToBeRegistered("Mario");

        // Then
        final Friend expectedMario = new Friend(new FriendId("Mario"));
        final FriendEntity marioEntity = runInTransaction(() -> entityManager.find(FriendEntity.class, "Mario"));
        assertThat(marioEntity).isNotNull();
        assertThat(marioEntity.toFriend()).isEqualTo(expectedMario);

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(1);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("ANewFriendRegisteredIntoThePlatform".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("Mario".getBytes());

        assertThat(arangoDBFriends.getFriend("Mario")).isEqualTo(expectedMario);
    }

    @Test
    @Order(3)
    public void should_register_peach_as_a_new_friend_into_the_platform() throws Exception {
        // TODO replace by a message consumed from kafka
        // Given

        // When
        keycloakAdminClient.register("Peach");
        waitForFriendToBeRegistered("Peach");

        // Then
        final Friend expectedPeach = new Friend(new FriendId("Peach"));
        final FriendEntity peachEntity = runInTransaction(() -> entityManager.find(FriendEntity.class, "Peach"));
        assertThat(peachEntity).isNotNull();
        assertThat(peachEntity.toFriend()).isEqualTo(expectedPeach);

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(1);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("ANewFriendRegisteredIntoThePlatform".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("Peach".getBytes());

        assertThat(arangoDBFriends.getFriend("Peach")).isEqualTo(expectedPeach);
    }

    @Test
    @Order(4)
    public void should_register_Luigi_as_a_new_friend_into_the_platform() throws Exception {
        // Given

        // When
        keycloakAdminClient.register("Luigi");
        waitForFriendToBeRegistered("Luigi");

        // Then
        final Friend expectedLuigi = new Friend(new FriendId("Luigi"));
        final FriendEntity luigiEntity = runInTransaction(() -> entityManager.find(FriendEntity.class, "Luigi"));
        assertThat(luigiEntity).isNotNull();
        assertThat(luigiEntity.toFriend()).isEqualTo(expectedLuigi);

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(1);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("ANewFriendRegisteredIntoThePlatform".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("Luigi".getBytes());

        assertThat(arangoDBFriends.getFriend("Luigi")).isEqualTo(expectedLuigi);
    }

    @Test
    @Order(5)
    public void should_mario_generate_an_invitation_code() throws Exception {
        // Given

        // When
        final UUID invitationCode = UUID.fromString(
                given()
                        .when()
                        .post("/friends/Mario/generateInvitationCode")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().path("invitationCode"));

        // Then
        final InvitationCodeGeneratedEntity invitationCodeGeneratedEntity = runInTransaction(() -> entityManager.find(InvitationCodeGeneratedEntity.class, invitationCode));
        assertThat(invitationCodeGeneratedEntity.fromFriendId).isEqualTo("Mario");
        assertThat(invitationCodeGeneratedEntity.generatedAt).isBeforeOrEqualTo(LocalDateTime.now(ZoneOffset.UTC));

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(1);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("InvitationCodeGenerated".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("Mario".getBytes());

        final TracesUtils.Traces traces = tracesUtils.getTraces("/friends/Mario/generateInvitationCode");
        assertThat(traces.getOperationNames()).containsExactlyInAnyOrder(
                "friends/{fromFriendId}/generateInvitationCode",
                "PostgresInvitationCodeGeneratedRepository:store");
        assertThat(traces.getHttpStatus()).containsExactlyInAnyOrder(200);
        assertThat(traces.getOperationNamesInError()).isEmpty();
    }

    @Test
    @Order(6)
    public void should_establish_a_relation_ship_from_peach_to_mario_using_mario_invitation_code() throws Exception {
        // Given
        final InvitationCodeGeneratedEntity invitationCodeGeneratedEntity = runInTransaction(() ->
                entityManager
                        .createQuery("SELECT i FROM InvitationCodeGeneratedEntity i WHERE i.fromFriendId = :fromFriendId", InvitationCodeGeneratedEntity.class)
                        .setParameter("fromFriendId", "Mario").getSingleResult());

        // When
        given()
                .param("invitationCode", invitationCodeGeneratedEntity.invitationCode.toString())
                .param("executedBy", "Peach")
                .when()
                .post("/friends/Peach/establishAFriendship")
                .then()
                .log().all()
                .statusCode(200)
                .body("friends", containsInAnyOrder("Mario", "DamDamDeo"));

        // Then
        final FriendEntity peachEntity = entityManager.find(FriendEntity.class, "Peach");
        assertThat(peachEntity.toFriend()).isEqualTo(new Friend(new FriendId("Peach"))
                .establishAFriendship(new FriendId("Mario")));

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(2);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("ToFriendEstablishedAFriendshipWithFromFriend".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("Peach".getBytes());

        assertThat(friendsEvents.get(1).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(1).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(1).key().getBytes());
        assertThat(friendsEvents.get(1).headers().lastHeader("eventType").value()).isEqualTo("FromFriendEstablishedAFriendshipWithToFriend".getBytes());
        assertThat(friendsEvents.get(1).headers().lastHeader("aggregateId").value()).isEqualTo("Mario".getBytes());


        final Friend peachFromArango = arangoDBFriends.getFriendWithRelationship("Peach", "Mario");
        assertThat(peachFromArango).isEqualTo(peachEntity.toFriend());

        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
                .until(() -> runInTransaction(() -> entityManager.find(FriendEntity.class, "Mario"))
                        .toFriend().inFriendshipsWith().contains(new InFriendshipWithId("Peach")));

        final FriendEntity marioEntity = runInTransaction(() -> entityManager.find(FriendEntity.class, "Mario"));
        assertThat(marioEntity.toFriend()).isEqualTo(
                new Friend(new FriendId("Mario"))
                        .establishAFriendship(new FriendId("Peach")));

        final Friend marioFromArango = arangoDBFriends.getFriendWithRelationship("Mario", "Peach");
        assertThat(marioFromArango).isEqualTo(marioEntity.toFriend());

        final TracesUtils.Traces traces = tracesUtils.getTraces("/friends/Peach/establishAFriendship");
        assertThat(traces.getOperationNames()).containsExactlyInAnyOrder(
                "friends/{toFriendId}/establishAFriendship",
                "PostgresInvitationCodeGeneratedRepository:get",
                "PostgresFriendRepository:getBy",
                "PostgresFriendRepository:save");
        assertThat(traces.getHttpStatus()).containsExactlyInAnyOrder(200);
        assertThat(traces.getOperationNamesInError()).isEmpty();
    }

    @Test
    @Order(7)
    public void should_establish_a_relation_ship_from_luigi_to_mario_using_mario_invitation_code() throws Exception {
        // Given
        final InvitationCodeGeneratedEntity invitationCodeGeneratedEntity = runInTransaction(() ->
                entityManager
                        .createQuery("SELECT i FROM InvitationCodeGeneratedEntity i WHERE i.fromFriendId = :fromFriendId", InvitationCodeGeneratedEntity.class)
                        .setParameter("fromFriendId", "Mario").getSingleResult());

        // When
        given()
                .param("invitationCode", invitationCodeGeneratedEntity.invitationCode.toString())
                .param("executedBy", "Luigi")
                .when()
                .post("/friends/Luigi/establishAFriendship")
                .then()
                .log().all()
                .statusCode(200)
                .body("friends", containsInAnyOrder("Mario", "DamDamDeo"));

        // Then
        final FriendEntity luigiEntity = entityManager.find(FriendEntity.class, "Luigi");
        assertThat(luigiEntity.toFriend()).isEqualTo(new Friend(new FriendId("Luigi"))
                .establishAFriendship(new FriendId("Mario")));

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(2);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("ToFriendEstablishedAFriendshipWithFromFriend".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("Luigi".getBytes());

        assertThat(friendsEvents.get(1).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(1).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(1).key().getBytes());
        assertThat(friendsEvents.get(1).headers().lastHeader("eventType").value()).isEqualTo("FromFriendEstablishedAFriendshipWithToFriend".getBytes());
        assertThat(friendsEvents.get(1).headers().lastHeader("aggregateId").value()).isEqualTo("Mario".getBytes());

        final Friend luigiFromArango = arangoDBFriends.getFriendWithRelationship("Luigi", "Mario");
        assertThat(luigiFromArango).isEqualTo(luigiEntity.toFriend());

        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS)
                .until(() -> runInTransaction(() -> entityManager.find(FriendEntity.class, "Mario"))
                        .toFriend().inFriendshipsWith().contains(new InFriendshipWithId("Luigi")));

        final FriendEntity marioEntity = runInTransaction(() -> entityManager.find(FriendEntity.class, "Mario"));
        assertThat(marioEntity.toFriend()).isEqualTo(
                new Friend(new FriendId("Mario"))
                        .establishAFriendship(new FriendId("Peach"))
                        .establishAFriendship(new FriendId("Luigi")));

        final Friend marioFromArango = arangoDBFriends.getFriendWithRelationship("Mario", "Luigi");
        assertThat(marioFromArango).isEqualTo(marioEntity.toFriend());

        final TracesUtils.Traces traces = tracesUtils.getTraces("/friends/Luigi/establishAFriendship");
        assertThat(traces.getOperationNames()).containsExactlyInAnyOrder(
                "friends/{toFriendId}/establishAFriendship",
                "PostgresInvitationCodeGeneratedRepository:get",
                "PostgresFriendRepository:getBy",
                "PostgresFriendRepository:save");
        assertThat(traces.getHttpStatus()).containsExactlyInAnyOrder(200);
        assertThat(traces.getOperationNamesInError()).isEmpty();
    }

    @Test
    @Order(8)
    public void should_list_mario_and_peach_mutual_friends() throws Exception {
        given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/Peach/mutualFriends")
                .then()
                .log().all()
                .statusCode(200)
                .body("mutualFriendsId", containsInAnyOrder("DamDamDeo"));
        final TracesUtils.Traces traces = tracesUtils.getTraces("/friends/Mario/inFriendshipsWith/Peach/mutualFriends");
        assertThat(traces.getOperationNames()).containsExactlyInAnyOrder(
                "friends/{friendId}/inFriendshipsWith/{inFriendshipWithId}/mutualFriends",
                "ArangoDBMutualFriendsRepository:getMutualFriends");
        assertThat(traces.getHttpStatus()).containsExactlyInAnyOrder(200);
        assertThat(traces.getOperationNamesInError()).isEmpty();
    }

    @Test
    @Order(9)
    public void should_list_mario_in_friendships_with() throws Exception {
        given()
                .when()
                .get("/friends/Mario")
                .then()
                .log().all()
                .statusCode(200)
                .body("friends", containsInAnyOrder("DamDamDeo", "Peach", "Luigi"));

        final TracesUtils.Traces traces = tracesUtils.getTraces("/friends/Mario");
        assertThat(traces.getOperationNames()).containsExactlyInAnyOrder(
                "friends/{friendId}",
                "PostgresFriendRepository:getBy");
        assertThat(traces.getHttpStatus()).containsExactlyInAnyOrder(200);
        assertThat(traces.getOperationNamesInError()).isEmpty();
    }

    @Test
    @Order(10)
    public void should_mario_write_is_bio() throws Exception {
        // Given

        // When
        given()
                .param("bio", "super plumber")
                .param("executedBy", "Mario")
                .when()
                .post("/friends/Mario/writeBio")
                .then()
                .log().all()
                .statusCode(200)
                .body("bio", equalTo("super plumber"));

        // Then
        final FriendEntity marioEntity = entityManager.find(FriendEntity.class, "Mario");
        assertThat(marioEntity.toFriend()).isEqualTo(new Friend(new FriendId("Mario"))
                .establishAFriendship(new FriendId("Peach"))
                .establishAFriendship(new FriendId("Luigi"))
                .writeBio(new Bio("super plumber"), new ExecutedBy("Mario")));

        final List<ConsumerRecord<String, JsonObject>> friendsEvents = topicConsumer.drain(1);
        assertThat(friendsEvents.get(0).key()).matches("\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b");
        assertThat(friendsEvents.get(0).headers().lastHeader("id").value()).isEqualTo(friendsEvents.get(0).key().getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("eventType").value()).isEqualTo("BioWritten".getBytes());
        assertThat(friendsEvents.get(0).headers().lastHeader("aggregateId").value()).isEqualTo("Mario".getBytes());

        // TODO should wait until bio is updated
        assertThat(arangoDBFriends.getFriend("Mario")).isEqualTo(marioEntity.toFriend());

        final TracesUtils.Traces traces = tracesUtils.getTraces("/friends/Mario/writeBio");
        assertThat(traces.getOperationNames()).containsExactlyInAnyOrder(
                "friends/{friendId}/writeBio",
                "PostgresFriendRepository:getBy",
                "PostgresFriendRepository:save");
        assertThat(traces.getHttpStatus()).containsExactlyInAnyOrder(200);
        assertThat(traces.getOperationNamesInError()).isEmpty();
    }

    private void waitForFriendToBeRegistered(final String friendId) {
        Awaitility.await()
                .atMost(Durations.TEN_SECONDS)
                .pollInterval(Durations.ONE_HUNDRED_MILLISECONDS).until(() ->
                    runInTransaction(() -> entityManager.find(FriendEntity.class, friendId)) != null);
    }

    // TODO DonkeyKong, Pauline !!!

    private <V> V runInTransaction(final Callable<V> callable) throws Exception {
        userTransaction.begin();
        try {
            return callable.call();
        } catch (final Exception exception) {
            throw exception;
        } finally {
            userTransaction.commit();
        }
    }

}
