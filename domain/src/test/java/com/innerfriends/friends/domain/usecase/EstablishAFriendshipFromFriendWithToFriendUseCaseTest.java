package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstablishAFriendshipFromFriendWithToFriendUseCaseTest {

    private FriendRepository friendRepository;
    private EstablishAFriendshipFromFriendWithToFriendUseCase establishAFriendshipFromFriendWithToFriendUseCase;

    @BeforeEach
    public void setup() {
        friendRepository = mock(FriendRepository.class);
        establishAFriendshipFromFriendWithToFriendUseCase = new EstablishAFriendshipFromFriendWithToFriendUseCase(friendRepository);
    }

    @Test
    public void should_connect_from_friend_with_to_friend() {
        // Given
        final FromFriendId fromFriendId = new FromFriendId("Mario");
        final ToFriendId toFriendId = new ToFriendId("Peach");
        final Friend friend = new Friend(new FriendId("Mario"));
        doReturn(friend).when(friendRepository).getBy(new FriendId("Mario"));
        final EstablishAFriendshipFromFriendWithToFriendCommand establishAFriendshipFromFriendWithToFriendCommand = new EstablishAFriendshipFromFriendWithToFriendCommand(fromFriendId, toFriendId);

        // When && Then
        final Friend expectedFriend = new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("DamDamDeo"),
                new InFriendshipWithId("Peach")), new Bio(), new Version(1L));
        assertThat(establishAFriendshipFromFriendWithToFriendUseCase.execute(establishAFriendshipFromFriendWithToFriendCommand))
                .isEqualTo(expectedFriend);
        verify(friendRepository, times(1)).save(expectedFriend);
    }

    @Test
    public void should_fail_fast_when_connecting_yourself() {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"));
        doReturn(friend).when(friendRepository).getBy(new FriendId("Mario"));
        final EstablishAFriendshipFromFriendWithToFriendCommand establishAFriendshipFromFriendWithToFriendCommand = new EstablishAFriendshipFromFriendWithToFriendCommand(
                new FromFriendId("Mario"), new ToFriendId("Mario"));

        // When && Then
        assertThatThrownBy(() -> establishAFriendshipFromFriendWithToFriendUseCase.execute(establishAFriendshipFromFriendWithToFriendCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("You could not add yourself as a friend");
    }

    @Test
    public void should_fail_when_friend_already_established() {
        // Given
        final Friend friend = new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("Peach")), new Bio(), new Version(1l));
        doReturn(friend).when(friendRepository).getBy(new FriendId("Mario"));
        final EstablishAFriendshipFromFriendWithToFriendCommand establishAFriendshipFromFriendWithToFriendCommand = new EstablishAFriendshipFromFriendWithToFriendCommand(
                new FromFriendId("Mario"), new ToFriendId("Peach"));

        // When && Then
        assertThatThrownBy(() -> establishAFriendshipFromFriendWithToFriendUseCase.execute(establishAFriendshipFromFriendWithToFriendCommand))
                .isInstanceOf(AlreadyAFriendException.class);
    }

}
