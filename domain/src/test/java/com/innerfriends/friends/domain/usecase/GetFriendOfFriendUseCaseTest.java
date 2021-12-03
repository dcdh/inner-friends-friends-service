package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class GetFriendOfFriendUseCaseTest {

    private FriendRepository friendRepository;

    @BeforeEach
    public void setup() {
        friendRepository = mock(FriendRepository.class);
    }

    @Test
    public void should_get_friend_of_friend() {
        // Given
        doReturn(new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("Donkey Kong")), new Bio(), new Version(1l)))
                .when(friendRepository).getBy(new FriendId("Mario"));
        doReturn(new Friend(new FriendId("Donkey Kong"), List.of(new InFriendshipWithId("Pauline")), new Bio(), new Version(1l)))
                .when(friendRepository).getBy(new FriendId("Donkey Kong"));
        doReturn(new Friend(new FriendId("Pauline")))
                .when(friendRepository).getBy(new FriendId("Pauline"));

        final GetFriendOfFriendUseCase getFriendOfFriendUseCase = new GetFriendOfFriendUseCase(friendRepository);
        final GetFriendOfFriendCommand getFriendOfFriendCommand = new GetFriendOfFriendCommand(
                new FriendId("Mario"), new InFriendshipWithId("Donkey Kong"), new FriendOfFriendId("Pauline"));

        // When
        final FriendOfFriend friendOfFriend = getFriendOfFriendUseCase.execute(getFriendOfFriendCommand);

        // Then
        assertThat(friendOfFriend).isEqualTo(new FriendOfFriend(new FriendOfFriendId("Pauline"), new Bio(), new Version(0l), false));
    }

    @Test
    public void should_get_in_relation_ship_friends_of_friends() {
        // Given
        doReturn(new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("Donkey Kong"), new InFriendshipWithId("Pauline")),
                new Bio(), new Version(1l)))
                .when(friendRepository).getBy(new FriendId("Mario"));
        doReturn(new Friend(new FriendId("Donkey Kong"), List.of(new InFriendshipWithId("Pauline")), new Bio(), new Version(1l)))
                .when(friendRepository).getBy(new FriendId("Donkey Kong"));
        doReturn(new Friend(new FriendId("Pauline")))
                .when(friendRepository).getBy(new FriendId("Pauline"));

        final GetFriendOfFriendUseCase getFriendOfFriendUseCase = new GetFriendOfFriendUseCase(friendRepository);
        final GetFriendOfFriendCommand getFriendOfFriendCommand = new GetFriendOfFriendCommand(
                new FriendId("Mario"), new InFriendshipWithId("Donkey Kong"), new FriendOfFriendId("Pauline"));

        // When
        final FriendOfFriend friendOfFriend = getFriendOfFriendUseCase.execute(getFriendOfFriendCommand);

        // Then
        assertThat(friendOfFriend).isEqualTo(new FriendOfFriend(new FriendOfFriendId("Pauline"), new Bio(), new Version(0l), true));
    }

    @Test
    public void should_fail_fast_when_not_in_friendship_with() {
        // Given
        doReturn(new Friend(new FriendId("Mario"), List.of(), new Bio(), new Version(1l)))
                .when(friendRepository).getBy(new FriendId("Mario"));

        final GetFriendOfFriendUseCase getFriendOfFriendUseCase = new GetFriendOfFriendUseCase(friendRepository);
        final GetFriendOfFriendCommand getFriendOfFriendCommand = new GetFriendOfFriendCommand(
                new FriendId("Mario"), new InFriendshipWithId("Donkey Kong"), new FriendOfFriendId("Pauline"));

        // When && Then
        assertThatThrownBy(() -> getFriendOfFriendUseCase.execute(getFriendOfFriendCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not in friendship with");
    }

    @Test
    public void should_fail_fast_when_not_a_friend_of_friend() {
        // Given
        doReturn(new Friend(new FriendId("Mario"), List.of(new InFriendshipWithId("Donkey Kong")), new Bio(), new Version(1l)))
                .when(friendRepository).getBy(new FriendId("Mario"));
        doReturn(new Friend(new FriendId("Donkey Kong"), List.of(), new Bio(), new Version(1l)))
                .when(friendRepository).getBy(new FriendId("Donkey Kong"));

        final GetFriendOfFriendUseCase getFriendOfFriendUseCase = new GetFriendOfFriendUseCase(friendRepository);
        final GetFriendOfFriendCommand getFriendOfFriendCommand = new GetFriendOfFriendCommand(
                new FriendId("Mario"), new InFriendshipWithId("Donkey Kong"), new FriendOfFriendId("Pauline"));

        // When && Then
        assertThatThrownBy(() -> getFriendOfFriendUseCase.execute(getFriendOfFriendCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Not a friend of friend");
    }

}
