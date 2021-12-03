package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class GetInFriendshipWithUseCaseTest {

    private FriendRepository friendRepository;
    private GetInFriendshipWithUseCase getInFriendshipWithUseCase;

    @BeforeEach
    public void setup() {
        friendRepository = mock(FriendRepository.class);
        getInFriendshipWithUseCase = new GetInFriendshipWithUseCase(friendRepository);
    }

    @Test
    public void should_get_in_friendship_with() {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));
        doReturn(friend).when(friendRepository).getBy(new FriendId("Mario"));
        final GetInFriendshipWithCommand getInFriendshipWithCommand = new GetInFriendshipWithCommand(new FriendId("Mario"),
                new InFriendshipWithId("DamDamDeo"));
        doReturn(new Friend(new FriendId("DamDamDeo"))).when(friendRepository).getBy(new FriendId("DamDamDeo"));

        // When && Then
        assertThat(getInFriendshipWithUseCase.execute(getInFriendshipWithCommand))
                .isEqualTo(new InFriendshipWith(new Friend(new FriendId("DamDamDeo"))));
    }

    @Test
    public void should_fail_fast_when_friendship_not_established() {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));
        doReturn(friend).when(friendRepository).getBy(new FriendId("Mario"));
        final GetInFriendshipWithCommand getInFriendshipWithCommand = new GetInFriendshipWithCommand(new FriendId("Mario"),
                new InFriendshipWithId("Bowser"));

        // When && Then
        assertThatThrownBy(() -> getInFriendshipWithUseCase.execute(getInFriendshipWithCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not in friendship with");
    }

}
