package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterNewFriendIntoThePlatformUseCaseTest {

    @Test
    public void should_register_a_new_friend_into_the_platform() {
        // Given
        final FriendRepository friendRepository = mock(FriendRepository.class);
        final RegisterNewFriendIntoThePlatformUseCase registerNewFriendIntoThePlatformUseCase
                = new RegisterNewFriendIntoThePlatformUseCase(friendRepository);
        final RegisterNewFriendIntoThePlatformCommand registerNewFriendIntoThePlatformCommand = new RegisterNewFriendIntoThePlatformCommand(
                new NewPseudoId("Mario"));

        // When
        final Friend newFriendRegistered = registerNewFriendIntoThePlatformUseCase.execute(registerNewFriendIntoThePlatformCommand);

        // Then
        final Friend expectedFriend = new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("DamDamDeo")), new Bio(), new Version(0l));
        assertThat(newFriendRegistered).isEqualTo(expectedFriend);
        verify(friendRepository, times(1)).create(expectedFriend);
    }

}
