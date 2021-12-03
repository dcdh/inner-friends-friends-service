package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class GetInFriendshipWithMutualFriendsUseCaseTest {

    @Test
    public void should_get_mutual_friends() {
        // Given
        final MutualFriendsRepository mutualFriendsRepository = mock(MutualFriendsRepository.class);
        final FriendId mario = new FriendId("Mario");
        final InFriendshipWithId luigi = new InFriendshipWithId("Luigi");
        doReturn(List.of(new MutualFriendId("Toad"))).when(mutualFriendsRepository).getMutualFriends(mario, luigi);
        final GetInFriendshipWithMutualFriendsUseCase getInFriendshipWithMutualFriendsUseCase = new GetInFriendshipWithMutualFriendsUseCase(mutualFriendsRepository);
        final GetInFriendshipWithMutualFriendsCommand getInFriendshipWithMutualFriendsCommand = new GetInFriendshipWithMutualFriendsCommand(mario, luigi);

        // When
        final InFriendshipWithMutualFriends inFriendshipWithMutualFriends = getInFriendshipWithMutualFriendsUseCase.execute(getInFriendshipWithMutualFriendsCommand);

        // Then
        assertThat(inFriendshipWithMutualFriends).isEqualTo(new InFriendshipWithMutualFriends(
                mario, luigi, List.of(new MutualFriendId("Toad"))));
    }

}
