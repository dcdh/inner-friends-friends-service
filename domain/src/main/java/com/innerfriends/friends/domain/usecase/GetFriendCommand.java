package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class GetFriendCommand implements UseCaseCommand<FriendId> {

    private final FriendId friendId;

    public GetFriendCommand(final FriendId friendId) {
        this.friendId = Objects.requireNonNull(friendId);
    }

    public FriendId friendId() {
        return friendId;
    }

    @Override
    public FriendId identifier() {
        return friendId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GetFriendCommand)) return false;
        final GetFriendCommand that = (GetFriendCommand) o;
        return Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId);
    }

    @Override
    public String toString() {
        return "GetFriendCommand{" +
                "friendId=" + friendId +
                '}';
    }
}
