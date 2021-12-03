package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendOfFriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GetFriendsOfFriendMutualFriendsCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(GetFriendsOfFriendMutualFriendsCommand.class).verify();
    }

    @Test
    public void should_identifier_return_friend_id() {
        // Given
        final FriendId friendId = mock(FriendId.class);

        // When && Then
        assertThat(new GetFriendsOfFriendMutualFriendsCommand(friendId, mock(InFriendshipWithId.class), mock(FriendOfFriendId.class))
                .identifier()).isEqualTo(friendId);
    }
}
