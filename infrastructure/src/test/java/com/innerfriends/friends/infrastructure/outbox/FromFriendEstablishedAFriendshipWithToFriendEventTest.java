package com.innerfriends.friends.infrastructure.outbox;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.approvaltests.Approvals.verifyJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class FromFriendEstablishedAFriendshipWithToFriendEventTest extends EventTest {

    @Test
    public void should_return_expected_event() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        mario.establishAFriendship(new FriendId("Luigi"));
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When
        final FromFriendEstablishedAFriendshipWithToFriendEvent fromFriendEstablishedAFriendshipWithToFriendEvent
                = FromFriendEstablishedAFriendshipWithToFriendEvent.of(mario, objectMapper, instantProvider);

        // Then
        assertThat(fromFriendEstablishedAFriendshipWithToFriendEvent.getAggregateId()).isEqualTo("Mario");
        assertThat(fromFriendEstablishedAFriendshipWithToFriendEvent.getAggregateType()).isEqualTo("Friend");
        assertThat(fromFriendEstablishedAFriendshipWithToFriendEvent.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1));
        assertThat(fromFriendEstablishedAFriendshipWithToFriendEvent.getType()).isEqualTo("FromFriendEstablishedAFriendshipWithToFriend");
        verifyJson(fromFriendEstablishedAFriendshipWithToFriendEvent.getPayload().toString());
    }

}