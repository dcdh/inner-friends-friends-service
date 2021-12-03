package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendOfFriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GetFriendOfFriendCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(GetFriendOfFriendCommand.class).verify();
    }

    @Test
    public void should_identifier_return_friend_of_friend_id() {
        // Given
        final FriendOfFriendId friendOfFriendId = mock(FriendOfFriendId.class);

        // When && Then
        assertThat(new GetFriendOfFriendCommand(mock(FriendId.class), mock(InFriendshipWithId.class), friendOfFriendId).identifier()).isEqualTo(friendOfFriendId);
    }

}
