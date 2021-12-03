package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class GetFriendUseCaseTest {

    @Test
    public void should_get_friend() {
        // Given
        final FriendRepository friendRepository = mock(FriendRepository.class);
        final GetFriendUseCase getFriendUseCase = new GetFriendUseCase(friendRepository);
        final GetFriendCommand getFriendCommand = new GetFriendCommand(new FriendId("Mario"));
        doReturn(new Friend(new FriendId("Mario"))).when(friendRepository).getBy(new FriendId("Mario"));

        // When
        final Friend friendRetrieved = getFriendUseCase.execute(getFriendCommand);

        // Then
        assertThat(friendRetrieved).isEqualTo(new Friend(new FriendId("Mario")));
    }

}
