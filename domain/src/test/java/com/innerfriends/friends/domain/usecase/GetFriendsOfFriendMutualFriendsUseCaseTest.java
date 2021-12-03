package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class GetFriendsOfFriendMutualFriendsUseCaseTest {

    @Test
    public void should_get_mutual_friends() {
        // Given
        final MutualFriendsRepository mutualFriendsRepository = mock(MutualFriendsRepository.class);
        final FriendId mario = new FriendId("Mario");
        final InFriendshipWithId donkeyKong = new InFriendshipWithId("Donkey Kong");
        final FriendOfFriendId pauline = new FriendOfFriendId("Pauline");
        doReturn(List.of(new MutualFriendId("Pauline"))).when(mutualFriendsRepository).getMutualFriends(mario, donkeyKong, pauline);
        final GetFriendsOfFriendMutualFriendsUseCase getFriendsOfFriendMutualFriendsUseCase = new GetFriendsOfFriendMutualFriendsUseCase(mutualFriendsRepository);
        final GetFriendsOfFriendMutualFriendsCommand getFriendsOfFriendMutualFriendsCommand = new GetFriendsOfFriendMutualFriendsCommand(mario, donkeyKong, pauline);

        // When
        final FriendsOfFriendMutualFriends friendsOfFriendMutualFriends = getFriendsOfFriendMutualFriendsUseCase.execute(getFriendsOfFriendMutualFriendsCommand);

        // Then
        assertThat(friendsOfFriendMutualFriends).isEqualTo(
                new FriendsOfFriendMutualFriends(mario, donkeyKong, pauline, List.of(new MutualFriendId("Pauline"))));
    }
}