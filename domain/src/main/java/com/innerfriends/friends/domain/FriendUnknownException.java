package com.innerfriends.friends.domain;

import java.util.Objects;

public class FriendUnknownException extends RuntimeException {

    private final FriendId friendId;

    public FriendUnknownException(final FriendId friendId) {
        this.friendId = Objects.requireNonNull(friendId);
    }

    public FriendId friendId() {
        return friendId;
    }

}
