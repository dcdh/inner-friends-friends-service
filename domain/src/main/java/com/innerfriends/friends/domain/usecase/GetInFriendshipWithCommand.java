package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

// TODO on BFF side check that executedBy is equals to friendId
public final class GetInFriendshipWithCommand implements UseCaseCommand<FriendId> {

    private final FriendId friendId;

    private final InFriendshipWithId inFriendshipWithId;

    public GetInFriendshipWithCommand(final FriendId friendId, final InFriendshipWithId inFriendshipWithId) {
        this.friendId = Objects.requireNonNull(friendId);
        this.inFriendshipWithId = Objects.requireNonNull(inFriendshipWithId);
    }

    @Override
    public FriendId identifier() {
        return friendId;
    }

    public FriendId friendId() {
        return friendId;
    }

    public InFriendshipWithId inFriendshipWithId() {
        return inFriendshipWithId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GetInFriendshipWithCommand)) return false;
        final GetInFriendshipWithCommand that = (GetInFriendshipWithCommand) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(inFriendshipWithId, that.inFriendshipWithId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, inFriendshipWithId);
    }

    @Override
    public String toString() {
        return "GetInFriendshipWithCommand{" +
                "friendId=" + friendId +
                ", inFriendshipWithId=" + inFriendshipWithId +
                '}';
    }
}
