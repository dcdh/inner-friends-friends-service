package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.Objects;

// TODO on BFF side check that executedBy is in the list of friend
public class GetFriendUseCase implements UseCase<Friend, GetFriendCommand> {

    private final FriendRepository friendRepository;

    public GetFriendUseCase(final FriendRepository friendRepository) {
        this.friendRepository = Objects.requireNonNull(friendRepository);
    }

    @Override
    public Friend execute(final GetFriendCommand command) {
        return friendRepository.getBy(command.friendId());
    }

}
