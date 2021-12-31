package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ListFriendMayKnowCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(ListFriendMayKnowCommand.class).verify();
    }

    @Test
    public void should_identifier_return_friendId() {
        // Given
        final FriendId friendId = mock(FriendId.class);

        // When && Then
        assertThat(new ListFriendMayKnowCommand(friendId, 0l).identifier()).isEqualTo(friendId);
    }
}