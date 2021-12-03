package com.innerfriends.friends.infrastructure.outbox;

import com.innerfriends.friends.domain.Bio;
import com.innerfriends.friends.domain.ExecutedBy;
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
public class ToFriendEstablishedAFriendshipWithFromFriendEventTest extends EventTest {

    @Test
    public void should_return_expected_event() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        mario.writeBio(new Bio("super plumber"), new ExecutedBy("Mario"));
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When
        final ToFriendEstablishedAFriendshipWithFromFriendEvent toFriendEstablishedAFriendshipWithFromFriendEvent = ToFriendEstablishedAFriendshipWithFromFriendEvent
                .of(mario, objectMapper, instantProvider);

        // Then
        assertThat(toFriendEstablishedAFriendshipWithFromFriendEvent.getAggregateId()).isEqualTo("Mario");
        assertThat(toFriendEstablishedAFriendshipWithFromFriendEvent.getAggregateType()).isEqualTo("Friend");
        assertThat(toFriendEstablishedAFriendshipWithFromFriendEvent.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1));
        assertThat(toFriendEstablishedAFriendshipWithFromFriendEvent.getType()).isEqualTo("ToFriendEstablishedAFriendshipWithFromFriend");
        verifyJson(toFriendEstablishedAFriendshipWithFromFriendEvent.getPayload().toString());
    }

}