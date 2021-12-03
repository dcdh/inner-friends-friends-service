package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GetInFriendshipWithMutualFriendsCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(GetInFriendshipWithMutualFriendsCommand.class).verify();
    }

    @Test
    public void should_identifier_return_friend_id() {
        // Given
        final FriendId friendId = mock(FriendId.class);

        // When && Then
        assertThat(new GetInFriendshipWithMutualFriendsCommand(friendId, mock(InFriendshipWithId.class)).identifier()).isEqualTo(friendId);
    }

}
