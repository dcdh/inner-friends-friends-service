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
public class NewFriendRegisteredIntoThePlatformEventTest extends EventTest {

    @Test
    public void should_return_expected_event() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When
        final NewFriendRegisteredIntoThePlatformEvent newFriendRegisteredIntoThePlatformEvent = NewFriendRegisteredIntoThePlatformEvent
                .of(mario, objectMapper, instantProvider);

        // Then
        assertThat(newFriendRegisteredIntoThePlatformEvent.getAggregateId()).isEqualTo("Mario");
        assertThat(newFriendRegisteredIntoThePlatformEvent.getAggregateType()).isEqualTo("Friend");
        assertThat(newFriendRegisteredIntoThePlatformEvent.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1));
        assertThat(newFriendRegisteredIntoThePlatformEvent.getType()).isEqualTo("NewFriendRegisteredIntoThePlatform");
        verifyJson(newFriendRegisteredIntoThePlatformEvent.getPayload().toString());
    }
}
