package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipFromFriendWithToFriendCommand;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipFromFriendWithToFriendUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class ManagedEstablishAFriendshipFromFriendWithToFriendUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedEstablishAFriendshipFromFriendWithToFriendUseCase managedEstablishAFriendshipFromFriendWithToFriendUseCase;

    @InjectMock
    EstablishAFriendshipFromFriendWithToFriendUseCase establishAFriendshipFromFriendWithToFriendUseCase;

    @InjectMock
    InstantProvider instantProvider;

    @Test
    public void should_establish_a_friendship_from_friend_with_to_friend() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        mario.establishAFriendship(new FriendId("Peach"), new ExecutedBy("Mario"));
        final EstablishAFriendshipFromFriendWithToFriendCommand establishAFriendshipFromFriendWithToFriendCommand
                = new EstablishAFriendshipFromFriendWithToFriendCommand(new FromFriendId("Mario"), new ToFriendId("Peach"));
        doReturn(mario).when(establishAFriendshipFromFriendWithToFriendUseCase).execute(establishAFriendshipFromFriendWithToFriendCommand);
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When && Then
        assertThat(managedEstablishAFriendshipFromFriendWithToFriendUseCase.execute(establishAFriendshipFromFriendWithToFriendCommand))
                .isEqualTo(mario);
        assertThat(((Number) entityManager.createNativeQuery(COUNT_OUTBOX_EVENT_FOR_AGGREGATE_ID_SQL)
                .setParameter(1, "Mario")
                .getSingleResult()).longValue()).isEqualTo(1l);
        assertThat(entityManager.createNativeQuery(GET_TYPE_BY_AGGREGATE_ID)
                .setParameter(1, "Mario")
                .getSingleResult())
                .isEqualTo("FromFriendEstablishedAFriendshipWithToFriend");
    }

}
