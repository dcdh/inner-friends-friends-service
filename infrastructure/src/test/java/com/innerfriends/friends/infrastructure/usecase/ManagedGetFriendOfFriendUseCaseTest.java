package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.GetFriendOfFriendCommand;
import com.innerfriends.friends.domain.usecase.GetFriendOfFriendUseCase;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class ManagedGetFriendOfFriendUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedGetFriendOfFriendUseCase managedGetFriendOfFriendUseCase;

    @InjectMock
    GetFriendOfFriendUseCase getFriendOfFriendUseCase;

    @Test
    public void should_get_friend_of_friend() {
        // Given
        final FriendOfFriend friendOfFriend = new FriendOfFriend(new FriendOfFriendId("Pauline"),
                new Bio("Friend of Mario !"), new Version(2l), true);
        final GetFriendOfFriendCommand getFriendOfFriendCommand = new GetFriendOfFriendCommand(
                new FriendId("Mario"), new InFriendshipWithId("DonkeyKong"), new FriendOfFriendId("Pauline"));
        doReturn(friendOfFriend).when(getFriendOfFriendUseCase).execute(getFriendOfFriendCommand);

        // When && Then
        assertThat(managedGetFriendOfFriendUseCase.execute(getFriendOfFriendCommand)).isEqualTo(friendOfFriend);
    }

}
