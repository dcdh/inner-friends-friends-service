package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendMayKnowId;
import com.innerfriends.friends.domain.FriendMayKnowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ListFriendMayKnowUseCaseTest {

    @Test
    public void should_list_friend_may_know() {
        // Given
        final FriendMayKnowRepository friendMayKnowRepository = mock(FriendMayKnowRepository.class);
        final ListFriendMayKnowUseCase listFriendMayKnowUseCase = new ListFriendMayKnowUseCase(friendMayKnowRepository);
        final ListFriendMayKnowCommand listFriendMayKnowCommand = new ListFriendMayKnowCommand(new FriendId("Mario"), 2l);
        doReturn(List.of(new FriendMayKnowId("Peach"))).when(friendMayKnowRepository).mayKnow(new FriendId("Mario"), 2l);

        // When
        final List<FriendMayKnowId> friendMayKnowIds = listFriendMayKnowUseCase.execute(listFriendMayKnowCommand);

        // Then
        assertThat(friendMayKnowIds).contains(new FriendMayKnowId("Peach"));
    }

}