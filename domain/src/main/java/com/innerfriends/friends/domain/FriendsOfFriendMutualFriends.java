package com.innerfriends.friends.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FriendsOfFriendMutualFriends implements Projection {

    private final FriendId friendId;
    private final InFriendshipWithId inFriendshipWithId;
    private final FriendOfFriendId friendOfFriendId;

    private final List<MutualFriendId> mutualFriendsId;

    public FriendsOfFriendMutualFriends(final FriendId friendId,
                                        final InFriendshipWithId inFriendshipWithId,
                                        final FriendOfFriendId friendOfFriendId,
                                        final List<MutualFriendId> mutualFriendsId) {
        this.friendId = Objects.requireNonNull(friendId);
        this.inFriendshipWithId = Objects.requireNonNull(inFriendshipWithId);
        this.friendOfFriendId = Objects.requireNonNull(friendOfFriendId);
        this.mutualFriendsId = Objects.requireNonNull(mutualFriendsId
                .stream()
                .distinct()
                .filter(mutualFriendId -> !mutualFriendId.equals(new MutualFriendId(inFriendshipWithId.pseudoId())))
                .collect(Collectors.toList()));
    }

    public FriendId friendId() {
        return friendId;
    }

    public InFriendshipWithId inFriendshipWithId() {
        return inFriendshipWithId;
    }

    public FriendOfFriendId friendOfFriendId() {
        return friendOfFriendId;
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
        if (!(o instanceof FriendsOfFriendMutualFriends)) return false;
        final FriendsOfFriendMutualFriends that = (FriendsOfFriendMutualFriends) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(inFriendshipWithId, that.inFriendshipWithId) &&
                Objects.equals(friendOfFriendId, that.friendOfFriendId) &&
                Objects.equals(mutualFriendsId, that.mutualFriendsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, inFriendshipWithId, friendOfFriendId, mutualFriendsId);
    }

    @Override
    public String toString() {
        return "FriendsOfFriendMutualFriends{" +
                "friendId=" + friendId +
                ", inFriendshipWithId=" + inFriendshipWithId +
                ", friendOfFriendId=" + friendOfFriendId +
                ", mutualFriendsId=" + mutualFriendsId +
                '}';
    }
}
