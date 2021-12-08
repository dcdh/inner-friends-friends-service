package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;

import java.util.Objects;

public class GetFriendOfFriendUseCase implements UseCase<FriendOfFriend, GetFriendOfFriendCommand> {

    private final FriendRepository friendRepository;

    public GetFriendOfFriendUseCase(final FriendRepository friendRepository) {
        this.friendRepository = Objects.requireNonNull(friendRepository);
    }

    @Override
    public FriendOfFriend execute(final GetFriendOfFriendCommand command) {
        final Friend friend = friendRepository.getBy(command.friendId());
        if (!friend.isInFriendshipWith(command.inFriendshipWithId())) {
            throw new NotInFriendshipWithException();
        }
        final Friend friendship = friendRepository.getBy(new FriendId(command.inFriendshipWithId()));
        if (!friendship.isInFriendshipWith(new InFriendshipWithId(command.friendOfFriendId()))) {
            throw new NotAFriendOfFriendException();
        }
        final Friend friendOfFriend = friendRepository.getBy(new FriendId(command.friendOfFriendId()));
        return new FriendOfFriend(
                new FriendOfFriendId(friendOfFriend.friendId()),
                friendOfFriend.bio(),
                friendOfFriend.version(),
                friend.isInFriendshipWith(new InFriendshipWithId(friendOfFriend.friendId())));
    }

}
