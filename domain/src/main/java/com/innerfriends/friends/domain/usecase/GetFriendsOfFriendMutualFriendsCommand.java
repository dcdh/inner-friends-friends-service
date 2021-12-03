package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.FriendOfFriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class GetFriendsOfFriendMutualFriendsCommand implements UseCaseCommand<FriendId> {

    private final FriendId friendId;
    private final InFriendshipWithId inFriendshipWithId;
    private final FriendOfFriendId friendOfFriendId;

    public GetFriendsOfFriendMutualFriendsCommand(final FriendId friendId,
                                                  final InFriendshipWithId inFriendshipWithId,
                                                  final FriendOfFriendId friendOfFriendId) {
        this.friendId = Objects.requireNonNull(friendId);
        this.inFriendshipWithId = Objects.requireNonNull(inFriendshipWithId);
        this.friendOfFriendId = Objects.requireNonNull(friendOfFriendId);
    }

    @Override
    public FriendId identifier() {
        return friendId;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GetFriendsOfFriendMutualFriendsCommand)) return false;
        final GetFriendsOfFriendMutualFriendsCommand that = (GetFriendsOfFriendMutualFriendsCommand) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(inFriendshipWithId, that.inFriendshipWithId) &&
                Objects.equals(friendOfFriendId, that.friendOfFriendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, inFriendshipWithId, friendOfFriendId);
    }

    @Override
    public String toString() {
        return "GetFriendsOfFriendMutualFriendsCommand{" +
                "friendId=" + friendId +
                ", inFriendshipWithId=" + inFriendshipWithId +
                ", friendOfFriendId=" + friendOfFriendId +
                '}';
    }

}
