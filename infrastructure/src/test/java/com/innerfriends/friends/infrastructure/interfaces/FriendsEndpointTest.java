package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.*;
import com.innerfriends.friends.infrastructure.usecase.*;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.Claim;
import io.quarkus.test.security.jwt.JwtSecurity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.approvaltests.Approvals.verifyJson;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class FriendsEndpointTest {

    @InjectMock
    private ManagedGetFriendUseCase managedGetFriendUseCase;

    @InjectMock
    private ManagedGetInFriendshipWithUseCase managedGetInFriendshipWithUseCase;

    @InjectMock
    private ManagedGenerateInvitationCodeUseCase managedGenerateInvitationCodeUseCase;

    @InjectMock
    private ManagedWriteBioUseCase managedWriteBioUseCase;

    @InjectMock
    private ManagedEstablishAFriendshipToFriendWithFromFriendUseCase managedEstablishAFriendshipToFriendWithFromFriendUseCase;

    @InjectMock
    private ManagedGetFriendOfFriendUseCase managedGetFriendOfFriendUseCase;

    @InjectMock
    private ManagedGetInFriendshipWithMutualFriendsUseCase managedGetInFriendshipWithMutualFriendsUseCase;

    @InjectMock
    private ManagedGetFriendsOfFriendMutualFriendsUseCase managedGetFriendsOfFriendMutualFriendsUseCase;

    @InjectMock
    private GetFriendMayKnowUseCase getFriendMayKnowUseCase;

    @InjectMock
    private ListFriendMayKnowUseCase listFriendMayKnowUseCase;

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_friend() {
        // Given
        doReturn(new Friend(new FriendId("Mario")))
                .when(managedGetFriendUseCase)
                .execute(new GetFriendCommand(new FriendId("Mario")));

        // When && Then
        final String jsonResponse = given()
                .when()
                .get("/friends/Mario")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_friend_fail_when_friend_is_unknown() {
        // Given
        doThrow(new FriendUnknownException(new FriendId("Mario")))
                .when(managedGetFriendUseCase)
                .execute(new GetFriendCommand(new FriendId("Mario")));

        // When && Then
        given()
                .when()
                .get("/friends/Mario")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_generate_invitation_code() {
        // Given
        doReturn(new InvitationCodeGenerated(
                new FromFriendId("Mario"),
                new InvitationCode(new UUID(0, 0)),
                new GeneratedAt(LocalDateTime.of(2021, 11, 29, 0, 0))))
                .when(managedGenerateInvitationCodeUseCase)
                .execute(new GenerateInvitationCodeCommand(new FromFriendId("Mario")));

        // When && Then
        final String jsonResponse = given()
                .when()
                .post("/friends/generateInvitationCode")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_write_bio() {
        // Given
        doReturn(new Friend(new FriendId("Mario")).writeBio(new Bio("super plumber"), new ExecutedBy("Mario")))
                .when(managedWriteBioUseCase)
                .execute(new WriteBioCommand(new FriendId("Mario"), new Bio("super plumber"), new ExecutedBy("Mario")));

        // When && Then
        final String jsonResponse = given()
                .param("bio", "super plumber")
                .when()
                .post("/friends/writeBio")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "peach", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Peach")
    })
    public void should_establish_a_friendship_to_friend_with_from_friend() {
        // Given
        doReturn(new Friend(new FriendId("Mario")).establishAFriendship(new FriendId("Peach")))
                .when(managedEstablishAFriendshipToFriendWithFromFriendUseCase)
                .execute(new EstablishAFriendshipToFriendWithFromFriendCommand(
                        new ToFriendId("Peach"),
                        new InvitationCode(new UUID(0, 0)),
                        new ExecutedBy("Peach")));

        // When && Then
        final String jsonResponse = given()
                .param("invitationCode", "00000000-0000-0000-0000-000000000000")
                .when()
                .post("/friends/establishAFriendship")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "peach", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Peach")
    })
    public void should_establish_a_friendship_to_friend_with_from_friend_fail_when_already_a_friend() {
        // Given
        doThrow(new AlreadyAFriendException())
                .when(managedEstablishAFriendshipToFriendWithFromFriendUseCase)
                .execute(new EstablishAFriendshipToFriendWithFromFriendCommand(
                        new ToFriendId("Peach"),
                        new InvitationCode(new UUID(0, 0)),
                        new ExecutedBy("Peach")));

        // When && Then
        given()
                .param("invitationCode", "00000000-0000-0000-0000-000000000000")
                .when()
                .post("/friends/establishAFriendship")
                .then()
                .log().all()
                .statusCode(400)
                .body(equalTo("Already a friend"));
    }

    @Test
    @TestSecurity(user = "peach", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Peach")
    })
    public void should_establish_a_friendship_to_friend_with_from_friend_fail_when_invitation_code_does_not_exists() {
        // Given
        doThrow(new UnknownInvitationCodeException())
                .when(managedEstablishAFriendshipToFriendWithFromFriendUseCase)
                .execute(new EstablishAFriendshipToFriendWithFromFriendCommand(
                        new ToFriendId("Peach"),
                        new InvitationCode(new UUID(0, 0)),
                        new ExecutedBy("Peach")));

        // When && Then
        given()
                .param("invitationCode", "00000000-0000-0000-0000-000000000000")
                .when()
                .post("/friends/establishAFriendship")
                .then()
                .log().all()
                .statusCode(404)
                .body(equalTo("Unknown invitation code"));
    }

    @Test
    @TestSecurity(user = "peach", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Peach")
    })
    public void should_establish_a_friendship_to_friend_with_from_friend_fail_when_invitation_code_is_invalide() {
        // Given
        doThrow(new InvitationCodeInvalideException())
                .when(managedEstablishAFriendshipToFriendWithFromFriendUseCase)
                .execute(new EstablishAFriendshipToFriendWithFromFriendCommand(
                        new ToFriendId("Peach"),
                        new InvitationCode(new UUID(0, 0)),
                        new ExecutedBy("Peach")));

        // When && Then
        given()
                .param("invitationCode", "00000000-0000-0000-0000-000000000000")
                .param("executedBy", "Peach")
                .when()
                .post("/friends/establishAFriendship")
                .then()
                .log().all()
                .statusCode(400)
                .body(equalTo("Invitation code invalid. Please generate a new one."));
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_in_friendship_with() {
        // Given
        doReturn(new InFriendshipWith(
                new InFriendshipWithId("DonkeyKong"), List.of(new FriendOfFriendId("Pauline")),
                new Bio(""), new Version(2l)))
                .when(managedGetInFriendshipWithUseCase)
                .execute(new GetInFriendshipWithCommand(
                        new FriendId("Mario"), new InFriendshipWithId("DonkeyKong")));

        // When && Then
        final String jsonResponse = given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/DonkeyKong")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_in_friendship_with_fail_when_not_in_friendship() {
        // Given
        doThrow(new NotInFriendshipWithException())
                .when(managedGetInFriendshipWithUseCase)
                .execute(new GetInFriendshipWithCommand(
                        new FriendId("Mario"), new InFriendshipWithId("DonkeyKong")));

        // When && Then
        given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/DonkeyKong")
                .then()
                .log().all()
                .statusCode(400)
                .body(equalTo("Not in friendship with"));
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_in_friendship_with_mutual_friends() {
        // Given
        doReturn(new InFriendshipWithMutualFriends(
                new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"),
                List.of(new MutualFriendId("Pauline"))))
                .when(managedGetInFriendshipWithMutualFriendsUseCase)
                .execute(new GetInFriendshipWithMutualFriendsCommand(
                        new FriendId("Mario"), new InFriendshipWithId("DonkeyKong")));

        // When && Then
        final String jsonResponse = given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/DonkeyKong/mutualFriends")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_friends_of_friend() {
        // Given
        doReturn(new FriendOfFriend(new FriendOfFriendId("Pauline"), new Bio(""), new Version(1l), true))
                .when(managedGetFriendOfFriendUseCase)
                .execute(new GetFriendOfFriendCommand(
                        new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline")));

        // When && Then
        final String jsonResponse = given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/DonkeyKong/friendsOfFriend/Pauline")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_friends_of_friend_when_not_in_friendship_with() {
        // Given
        doThrow(new NotInFriendshipWithException())
                .when(managedGetFriendOfFriendUseCase)
                .execute(new GetFriendOfFriendCommand(
                        new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline")));

        // When && Then
        given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/DonkeyKong/friendsOfFriend/Pauline")
                .then()
                .log().all()
                .statusCode(400)
                .body(equalTo("Not in friendship with"));
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_friends_of_friend_when_not_a_friend_of_friend() {
        // Given
        doThrow(new NotAFriendOfFriendException())
                .when(managedGetFriendOfFriendUseCase)
                .execute(new GetFriendOfFriendCommand(
                        new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline")));

        // When && Then
        given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/DonkeyKong/friendsOfFriend/Pauline")
                .then()
                .log().all()
                .statusCode(400)
                .body(equalTo("Not a friend of friend"));
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_friends_of_friend_mutual_friends() {
        // Given
        doReturn(new FriendsOfFriendMutualFriends(
                new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline"),
                List.of(new MutualFriendId("Luigi"))))
                .when(managedGetFriendsOfFriendMutualFriendsUseCase)
                .execute(new GetFriendsOfFriendMutualFriendsCommand(
                        new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline")));

        // When && Then
        final String jsonResponse = given()
                .when()
                .get("/friends/Mario/inFriendshipsWith/DonkeyKong/friendsOfFriend/Pauline/mutualFriends")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_get_friend_may_know() {
        // Given
        doReturn(new FriendMayKnow(
                new FriendMayKnowId("Peach"),
                new Bio("I am a princess"),
                new Version(1l),
                List.of(new MutualFriendId("Luigi"))
        ))
                .when(getFriendMayKnowUseCase)
                .execute(new GetFriendMayKnowCommand(new FriendId("Mario"), new FriendMayKnowId("Peach")));

        // When && Then
        final String jsonResponse = given()
                .when()
                .get("/friends/mayKnow/Peach")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

    @Test
    @TestSecurity(user = "mario", roles = "friend")
    @JwtSecurity(claims = {
            @Claim(key = "pseudo", value = "Mario")
    })
    public void should_list_friends_may_know() {
        // Given
        doReturn(List.of(new FriendMayKnowId("Peach")))
                .when(listFriendMayKnowUseCase).execute(new ListFriendMayKnowCommand(new FriendId("Mario"), 2l));

        // When && Then
        final String jsonResponse = given()
                .param("nbOf", 2l)
                .when()
                .post("/friends/mayKnow")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().body().prettyPrint();
        verifyJson(jsonResponse);
    }

}
