package com.innerfriends.friends.domain;

import java.util.Objects;

public final class EstablishedFriendshipWith {

    private final InFriendshipWithId inFriendshipWithId;

    public EstablishedFriendshipWith(final InFriendshipWithId inFriendshipWithId) {
        this.inFriendshipWithId = Objects.requireNonNull(inFriendshipWithId);
    }

    public InFriendshipWithId inFriendshipWithId() {
        return inFriendshipWithId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof EstablishedFriendshipWith)) return false;
        final EstablishedFriendshipWith that = (EstablishedFriendshipWith) o;
        return Objects.equals(inFriendshipWithId, that.inFriendshipWithId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inFriendshipWithId);
    }

    @Override
    public String toString() {
        return "EstablishedFriendshipWith{" +
                "inFriendshipWithId=" + inFriendshipWithId +
                '}';
    }

}
