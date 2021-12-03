package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.usecase.GetFriendCommand;
import com.innerfriends.friends.domain.usecase.GetFriendUseCase;
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
public class ManagedGetFriendUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedGetFriendUseCase managedGetFriendUseCase;

    @InjectMock
    GetFriendUseCase getFriendUseCase;

    @Test
    public void should_get_friend() {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));
        final GetFriendCommand getFriendCommand = new GetFriendCommand(new FriendId("Mario"));
        doReturn(friend).when(getFriendUseCase).execute(getFriendCommand);

        // When && Then
        assertThat(managedGetFriendUseCase.execute(getFriendCommand)).isEqualTo(friend);
    }

}
