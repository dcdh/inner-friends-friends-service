package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipToFriendWithFromFriendCommand;
import com.innerfriends.friends.domain.usecase.EstablishAFriendshipToFriendWithFromFriendUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class ManagedEstablishAFriendshipToFriendWithFromFriendUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedEstablishAFriendshipToFriendWithFromFriendUseCase managedEstablishAFriendshipToFriendWithFromFriendUseCase;

    @InjectMock
    EstablishAFriendshipToFriendWithFromFriendUseCase establishAFriendshipToFriendWithFromFriendUseCase;

    @InjectMock
    InstantProvider instantProvider;

    @Test
    public void should_establish_a_friendship_to_friend_with_from_friend() {
        // Given
        final Friend peach = new Friend(new FriendId("Peach"));
        peach.establishAFriendship(new FriendId("Mario"));
        final EstablishAFriendshipToFriendWithFromFriendCommand establishAFriendshipToFriendWithFromFriendCommand
                = new EstablishAFriendshipToFriendWithFromFriendCommand(new ToFriendId("Peach"), new InvitationCode(new UUID(0, 0)), new ExecutedBy("Peach"));
        doReturn(peach).when(establishAFriendshipToFriendWithFromFriendUseCase).execute(establishAFriendshipToFriendWithFromFriendCommand);
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When && Then
        assertThat(managedEstablishAFriendshipToFriendWithFromFriendUseCase.execute(establishAFriendshipToFriendWithFromFriendCommand))
                .isEqualTo(peach);
        assertThat(((Number) entityManager.createNativeQuery(COUNT_OUTBOX_EVENT_FOR_AGGREGATE_ID_SQL)
                .setParameter(1, "Peach")
                .getSingleResult()).longValue()).isEqualTo(1l);
        assertThat(entityManager.createNativeQuery(GET_TYPE_BY_AGGREGATE_ID)
                .setParameter(1, "Peach")
                .getSingleResult())
                .isEqualTo("ToFriendEstablishedAFriendshipWithFromFriend");
    }

}
