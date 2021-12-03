package com.innerfriends.friends.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class InFriendshipWithMutualFriends implements Projection {

    private final FriendId friendId;
    private final InFriendshipWithId inFriendshipWithId;

    private final List<MutualFriendId> mutualFriendsId;

    public InFriendshipWithMutualFriends(final FriendId friendId,
                                         final InFriendshipWithId inFriendshipWithId,
                                         final List<MutualFriendId> mutualFriendsId) {
        this.friendId = Objects.requireNonNull(friendId);
        this.inFriendshipWithId = Objects.requireNonNull(inFriendshipWithId);
        this.mutualFriendsId = Objects.requireNonNull(mutualFriendsId.stream()
                .distinct()
                .collect(Collectors.toList()));
    }

    public FriendId friendId() {
        return friendId;
    }

    public InFriendshipWithId inFriendshipWithId() {
        return inFriendshipWithId;
    }

    public List<MutualFriendId> mutualFriendsId() {
        return mutualFriendsId;
    }

    public int nbOfMutualFriends() {
        return mutualFriendsId.size();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InFriendshipWithMutualFriends)) return false;
        final InFriendshipWithMutualFriends that = (InFriendshipWithMutualFriends) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(inFriendshipWithId, that.inFriendshipWithId) &&
                Objects.equals(mutualFriendsId, that.mutualFriendsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, inFriendshipWithId, mutualFriendsId);
    }
}
