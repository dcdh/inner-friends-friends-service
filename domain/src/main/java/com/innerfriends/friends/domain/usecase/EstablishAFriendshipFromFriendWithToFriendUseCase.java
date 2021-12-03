package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.Objects;

public class EstablishAFriendshipFromFriendWithToFriendUseCase implements UseCase<Friend, EstablishAFriendshipFromFriendWithToFriendCommand> {

    private final FriendRepository friendRepository;

    public EstablishAFriendshipFromFriendWithToFriendUseCase(final FriendRepository friendRepository) {
        this.friendRepository = Objects.requireNonNull(friendRepository);
    }

    @Override
    public Friend execute(final EstablishAFriendshipFromFriendWithToFriendCommand command) {
        final Friend friend = friendRepository.getBy(new FriendId(command.fromFriendId()));
        friend.establishAFriendship(new FriendId(command.toFriendId()));
        friendRepository.save(friend);
        return friend;
    }

}
