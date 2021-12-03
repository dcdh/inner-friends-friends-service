package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.Objects;

public class WriteBioUseCase implements UseCase<Friend, WriteBioCommand> {

    private final FriendRepository friendRepository;

    public WriteBioUseCase(final FriendRepository friendRepository) {
        this.friendRepository = Objects.requireNonNull(friendRepository);
    }

    @Override
    public Friend execute(final WriteBioCommand command) {
        final Friend friend = friendRepository.getBy(command.friendId());
        friend.writeBio(command.bio(), command.executedBy());
        friendRepository.save(friend);
        return friend;
    }

}
