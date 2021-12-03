package com.innerfriends.friends.domain;

import java.util.Objects;

public final class FriendOfFriend implements Projection {

    private final FriendOfFriendId friendOfFriendId;
    private final Bio bio;
    private final Version version;
    private final Boolean alreadyInFriendship;

    public FriendOfFriend(final FriendOfFriendId friendOfFriendId,
                          final Bio bio,
                          final Version version,
                          final Boolean alreadyInFriendship) {
        this.friendOfFriendId = Objects.requireNonNull(friendOfFriendId);
        this.bio = Objects.requireNonNull(bio);
        this.version = Objects.requireNonNull(version);
        this.alreadyInFriendship = Objects.requireNonNull(alreadyInFriendship);
    }

    public FriendOfFriendId friendOfFriendId() {
        return friendOfFriendId;
    }

    public Bio bio() {
        return bio;
    }

    public Version version() {
        return version;
    }

    public Boolean alreadyInFriendship() {
        return alreadyInFriendship;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendOfFriend)) return false;
        final FriendOfFriend that = (FriendOfFriend) o;
        return Objects.equals(friendOfFriendId, that.friendOfFriendId) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(version, that.version) &&
                Objects.equals(alreadyInFriendship, that.alreadyInFriendship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendOfFriendId, bio, version, alreadyInFriendship);
    }

    @Override
    public String toString() {
        return "FriendOfFriend{" +
                "friendOfFriendId=" + friendOfFriendId +
                ", bio=" + bio +
                ", version=" + version +
                ", alreadyInFriendship=" + alreadyInFriendship +
                '}';
    }
}
