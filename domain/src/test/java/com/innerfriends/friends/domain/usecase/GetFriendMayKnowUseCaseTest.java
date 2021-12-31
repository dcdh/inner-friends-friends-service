package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendMayKnow;
import com.innerfriends.friends.domain.FriendMayKnowId;
import com.innerfriends.friends.domain.FriendMayKnowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class GetFriendMayKnowUseCaseTest {

    @Test
    public void should_get_friend_may_know() {
        // Given
        final FriendMayKnowRepository friendMayKnowRepository = mock(FriendMayKnowRepository.class);
        final GetFriendMayKnowUseCase listFriendMayKnowUseCase = new GetFriendMayKnowUseCase(friendMayKnowRepository);
        final GetFriendMayKnowCommand getFriendMayKnowCommand = new GetFriendMayKnowCommand(new FriendId("Mario"), new FriendMayKnowId("Peach"));
        final FriendMayKnow friendMayKnow = mock(FriendMayKnow.class);
        doReturn(friendMayKnow).when(friendMayKnowRepository).get(new FriendId("Mario"), new FriendMayKnowId("Peach"));

        // When && Then
        assertThat(listFriendMayKnowUseCase.execute(getFriendMayKnowCommand)).isEqualTo(friendMayKnow);
    }

}