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
public class ANewFriendRegisteredIntoThePlatformEventTest extends EventTest {

    @Test
    public void should_return_expected_event() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When
        final ANewFriendRegisteredIntoThePlatformEvent aNewFriendRegisteredIntoThePlatformEvent = ANewFriendRegisteredIntoThePlatformEvent
                .of(mario, objectMapper, instantProvider);

        // Then
        assertThat(aNewFriendRegisteredIntoThePlatformEvent.getAggregateId()).isEqualTo("Mario");
        assertThat(aNewFriendRegisteredIntoThePlatformEvent.getAggregateType()).isEqualTo("Friend");
        assertThat(aNewFriendRegisteredIntoThePlatformEvent.getTimestamp()).isEqualTo(Instant.ofEpochSecond(1));
        assertThat(aNewFriendRegisteredIntoThePlatformEvent.getType()).isEqualTo("ANewFriendRegisteredIntoThePlatform");
        verifyJson(aNewFriendRegisteredIntoThePlatformEvent.getPayload().toString());
    }
}
