package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.Objects;

public class RegisterNewFriendIntoThePlatformUseCase implements UseCase<Friend, RegisterNewFriendIntoThePlatformCommand> {

    private final FriendRepository friendRepository;

    public RegisterNewFriendIntoThePlatformUseCase(final FriendRepository friendRepository) {
        this.friendRepository = Objects.requireNonNull(friendRepository);
    }

    @Override
    public Friend execute(final RegisterNewFriendIntoThePlatformCommand command) {
        final Friend friend = new Friend(new FriendId(command.newPseudoId()));
        this.friendRepository.create(friend);
        return friend;
    }

}
