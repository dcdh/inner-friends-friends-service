package com.innerfriends.friends.domain;

import java.util.List;
import java.util.Objects;

public final class FriendMayKnow implements Projection {

    private final FriendMayKnowId friendMayKnowId;
    private final Bio bio;
    private final Version version;
    private final List<MutualFriendId> mutualFriendsId;

    public FriendMayKnow(final FriendMayKnowId friendMayKnowId,
                         final Bio bio,
                         final Version version,
                         final List<MutualFriendId> mutualFriendsId) {
        this.friendMayKnowId = Objects.requireNonNull(friendMayKnowId);
        this.bio = Objects.requireNonNull(bio);
        this.version = Objects.requireNonNull(version);
        this.mutualFriendsId = Objects.requireNonNull(mutualFriendsId);
    }

    public FriendMayKnowId youMayKnowId() {
        return friendMayKnowId;
    }

    public Bio bio() {
        return bio;
    }

    public Version version() {
        return version;
    }

    public List<MutualFriendId> mutualFriendsId() {
        return mutualFriendsId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendMayKnow)) return false;
        final FriendMayKnow that = (FriendMayKnow) o;
        return Objects.equals(friendMayKnowId, that.friendMayKnowId) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(version, that.version) &&
                Objects.equals(mutualFriendsId, that.mutualFriendsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendMayKnowId, bio, version, mutualFriendsId);
    }

    @Override
    public String toString() {
        return "FriendMayKnow{" +
                "friendMayKnowId=" + friendMayKnowId +
                ", bio=" + bio +
                ", version=" + version +
                ", mutualFriendsId=" + mutualFriendsId +
                '}';
    }
}
