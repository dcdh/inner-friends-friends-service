package com.innerfriends.friends.domain;

import java.util.Objects;

public final class PseudoAlreadyUsedException extends RuntimeException {

    private final FriendId friendId;

    public PseudoAlreadyUsedException(final FriendId friendId) {
        this.friendId = Objects.requireNonNull(friendId);
    }

    public FriendId friendId() {
        return friendId;
    }

}
