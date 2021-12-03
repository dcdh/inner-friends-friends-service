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
public class WriteBioUseCaseTest {

    private FriendRepository friendRepository;
    private WriteBioUseCase writeBioUseCase;

    @BeforeEach
    public void setup() {
        friendRepository = mock(FriendRepository.class);
        writeBioUseCase = new WriteBioUseCase(friendRepository);
    }

    @Test
    public void should_write_bio() {
        // Given
        final WriteBioCommand writeBioCommand = new WriteBioCommand(new FriendId("Mario"),
                new Bio("super plumber"), new ExecutedBy("Mario"));
        doReturn(new Friend(new FriendId("Mario"))).when(friendRepository).getBy(new FriendId("Mario"));

        // When
        final Friend friendWithBio = writeBioUseCase.execute(writeBioCommand);

        // Then
        final Friend expectedFriend = new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("DamDamDeo")),
                new Bio("super plumber"), new Version(1l));
        assertThat(friendWithBio).isEqualTo(expectedFriend);
        verify(friendRepository, times(1)).save(expectedFriend);
    }

    @Test
    public void should_fail_fast_when_executed_by_is_not_the_friend() {
        // Given
        final WriteBioCommand writeBioCommand = new WriteBioCommand(new FriendId("Mario"),
                new Bio("super plumber"), new ExecutedBy("Bowser"));
        doReturn(new Friend(new FriendId("Mario"))).when(friendRepository).getBy(new FriendId("Mario"));

        // When && Then
        assertThatThrownBy(() -> writeBioUseCase.execute(writeBioCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("The one doing the action must be the friend");
    }

}
