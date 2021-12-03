package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class GetInFriendshipWithMutualFriendsCommand implements UseCaseCommand<FriendId> {

    private final FriendId friendId;
    private final InFriendshipWithId inFriendshipWithId;

    public GetInFriendshipWithMutualFriendsCommand(final FriendId friendId,
                                                   final InFriendshipWithId inFriendshipWithId) {
        this.friendId = Objects.requireNonNull(friendId);
        this.inFriendshipWithId = Objects.requireNonNull(inFriendshipWithId);
    }

    public FriendId fromFriendId() {
        return friendId;
    }

    public InFriendshipWithId toFriendId() {
        return inFriendshipWithId;
    }

    @Override
    public FriendId identifier() {
        return friendId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GetInFriendshipWithMutualFriendsCommand)) return false;
        final GetInFriendshipWithMutualFriendsCommand that = (GetInFriendshipWithMutualFriendsCommand) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(inFriendshipWithId, that.inFriendshipWithId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, inFriendshipWithId);
    }
}
