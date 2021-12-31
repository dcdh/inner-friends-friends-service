package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendMayKnowId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class GetFriendMayKnowCommand implements UseCaseCommand<FriendId> {

    private final FriendId friendId;

    private final FriendMayKnowId friendMayKnowId;

    public GetFriendMayKnowCommand(final FriendId friendId,
                                   final FriendMayKnowId friendMayKnowId) {
        this.friendId = Objects.requireNonNull(friendId);
        this.friendMayKnowId = Objects.requireNonNull(friendMayKnowId);
    }

    @Override
    public FriendId identifier() {
        return friendId;
    }

    public FriendId friendId() {
        return friendId;
    }

    public FriendMayKnowId friendMayKnowId() {
        return friendMayKnowId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GetFriendMayKnowCommand)) return false;
        final GetFriendMayKnowCommand that = (GetFriendMayKnowCommand) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(friendMayKnowId, that.friendMayKnowId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, friendMayKnowId);
    }
}
