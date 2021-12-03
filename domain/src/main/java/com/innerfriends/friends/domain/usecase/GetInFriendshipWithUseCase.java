package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;

import java.util.Objects;

public class GetInFriendshipWithUseCase implements UseCase<InFriendshipWith, GetInFriendshipWithCommand> {

    private final FriendRepository friendRepository;

    public GetInFriendshipWithUseCase(final FriendRepository friendRepository) {
        this.friendRepository = Objects.requireNonNull(friendRepository);
    }

    @Override
    public InFriendshipWith execute(final GetInFriendshipWithCommand command) {
        final Friend friend = friendRepository.getBy(command.friendId());
        if (!friend.isInFriendshipWith(command.inFriendshipWithId())) {
            throw new IllegalStateException("Not in friendship with");
        }
        return new InFriendshipWith(friendRepository.getBy(new FriendId(command.inFriendshipWithId())));
    }

}
