package com.innerfriends.friends.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class InFriendshipWith implements Projection {

    private final InFriendshipWithId inFriendshipWithId;
    private final List<FriendOfFriendId> friendsOfFriendId;
    private final Bio bio;
    private final Version version;

    public InFriendshipWith(final Friend friend) {
        this(new InFriendshipWithId(friend.friendId()),
                friend.inFriendshipsWith()
                        .stream()
                        .map(FriendOfFriendId::new)
                        .collect(Collectors.toList()),
                friend.bio(),
                friend.version());
    }

    public InFriendshipWith(final InFriendshipWithId inFriendshipWithId,
                            final List<FriendOfFriendId> friendsOfFriendId,
                            final Bio bio,
                            final Version version) {
        this.inFriendshipWithId = Objects.requireNonNull(inFriendshipWithId);
        this.friendsOfFriendId = Objects.requireNonNull(friendsOfFriendId);
        this.bio = Objects.requireNonNull(bio);
        this.version = Objects.requireNonNull(version);
    }

    public InFriendshipWithId inFriendshipWithId() {
        return inFriendshipWithId;
    }

    public List<FriendOfFriendId> friendsOfFriendId() {
        return friendsOfFriendId;
    }

    public Bio bio() {
        return bio;
    }

    public Version version() {
        return version;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InFriendshipWith)) return false;
        final InFriendshipWith that = (InFriendshipWith) o;
        return Objects.equals(inFriendshipWithId, that.inFriendshipWithId) &&
                Objects.equals(friendsOfFriendId, that.friendsOfFriendId) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inFriendshipWithId, friendsOfFriendId, bio, version);
    }

    @Override
    public String toString() {
        return "InFriendshipWith{" +
                "inFriendshipWithId=" + inFriendshipWithId +
                ", friendsOfFriendId=" + friendsOfFriendId +
                ", bio=" + bio +
                ", version=" + version +
                '}';
    }
}
