package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class ListFriendMayKnowCommand implements UseCaseCommand<FriendId> {

    private final FriendId friendId;
    private final Long nbOf;

    public ListFriendMayKnowCommand(final FriendId friendId, final Long nbOf) {
        this.friendId = Objects.requireNonNull(friendId);
        this.nbOf = Objects.requireNonNull(nbOf);
    }

    @Override
    public FriendId identifier() {
        return friendId;
    }

    public FriendId friendId() {
        return friendId;
    }

    public Long nbOf() {
        return nbOf;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ListFriendMayKnowCommand)) return false;
        final ListFriendMayKnowCommand that = (ListFriendMayKnowCommand) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(nbOf, that.nbOf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, nbOf);
    }
}
