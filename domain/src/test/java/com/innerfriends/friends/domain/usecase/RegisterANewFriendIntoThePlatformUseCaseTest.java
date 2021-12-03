package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterANewFriendIntoThePlatformUseCaseTest {

    @Test
    public void should_register_a_new_friend_into_the_platform() {
        // Given
        final FriendRepository friendRepository = mock(FriendRepository.class);
        final RegisterANewFriendIntoThePlatformUseCase registerANewFriendIntoThePlatformUseCase
                = new RegisterANewFriendIntoThePlatformUseCase(friendRepository);
        final RegisterANewFriendIntoThePlatformCommand registerANewFriendIntoThePlatformCommand = new RegisterANewFriendIntoThePlatformCommand(
                new NewPseudoId("Mario"));

        // When
        final Friend newFriendRegistered = registerANewFriendIntoThePlatformUseCase.execute(registerANewFriendIntoThePlatformCommand);

        // Then
        final Friend expectedFriend = new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("DamDamDeo")), new Bio(), new Version(0l));
        assertThat(newFriendRegistered).isEqualTo(expectedFriend);
        verify(friendRepository, times(1)).create(expectedFriend);
    }

}
