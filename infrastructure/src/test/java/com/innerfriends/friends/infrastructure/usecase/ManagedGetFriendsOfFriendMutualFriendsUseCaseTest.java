package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.GetFriendsOfFriendMutualFriendsCommand;
import com.innerfriends.friends.domain.usecase.GetFriendsOfFriendMutualFriendsUseCase;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class ManagedGetFriendsOfFriendMutualFriendsUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedGetFriendsOfFriendMutualFriendsUseCase managedGetFriendsOfFriendMutualFriendsUseCase;

    @InjectMock
    GetFriendsOfFriendMutualFriendsUseCase getFriendsOfFriendMutualFriendsUseCase;

    @Test
    public void should_get_friends_of_friend_mutual_friends() {
        // Given
        final FriendsOfFriendMutualFriends friendsOfFriendMutualFriends = new FriendsOfFriendMutualFriends(
                new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline"), List.of(new MutualFriendId("Luigi")));
        final GetFriendsOfFriendMutualFriendsCommand getFriendsOfFriendMutualFriendsCommand
                = new GetFriendsOfFriendMutualFriendsCommand(new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline"));
        doReturn(friendsOfFriendMutualFriends).when(getFriendsOfFriendMutualFriendsUseCase).execute(getFriendsOfFriendMutualFriendsCommand);

        // When && Then
        assertThat(managedGetFriendsOfFriendMutualFriendsUseCase.execute(getFriendsOfFriendMutualFriendsCommand)).isEqualTo(friendsOfFriendMutualFriends);
    }

}