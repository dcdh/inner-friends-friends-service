package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.*;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RegisterForReflection
public class FriendsOfFriendMutualFriendsDTO {

    public final String friendId;
    public final String inFriendshipWithId;
    public final String friendOfFriendId;

    public final List<String> mutualFriendsId;
    public final Integer nbOfMutualFriends;

    public FriendsOfFriendMutualFriendsDTO(final FriendsOfFriendMutualFriends friendsOfFriendMutualFriends) {
        this.friendId = friendsOfFriendMutualFriends.friendId().pseudo();
        this.inFriendshipWithId = friendsOfFriendMutualFriends.inFriendshipWithId().pseudo();
        this.friendOfFriendId = friendsOfFriendMutualFriends.friendOfFriendId().pseudo();
        this.mutualFriendsId = friendsOfFriendMutualFriends.mutualFriendsId()
                .stream()
                .map(MutualFriendId::pseudo)
                .collect(Collectors.toList());
        this.nbOfMutualFriends = friendsOfFriendMutualFriends.nbOfMutualFriends();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendsOfFriendMutualFriendsDTO)) return false;
        final FriendsOfFriendMutualFriendsDTO that = (FriendsOfFriendMutualFriendsDTO) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(inFriendshipWithId, that.inFriendshipWithId) &&
                Objects.equals(friendOfFriendId, that.friendOfFriendId) &&
                Objects.equals(mutualFriendsId, that.mutualFriendsId) &&
                Objects.equals(nbOfMutualFriends, that.nbOfMutualFriends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, inFriendshipWithId, friendOfFriendId, mutualFriendsId, nbOfMutualFriends);
    }

    @Override
    public String toString() {
        return "FriendsOfFriendMutualFriendsDTO{" +
                "friendId='" + friendId + '\'' +
                ", inFriendshipWithId='" + inFriendshipWithId + '\'' +
                ", friendOfFriendId='" + friendOfFriendId + '\'' +
                ", mutualFriendsId=" + mutualFriendsId +
                ", nbOfMutualFriends=" + nbOfMutualFriends +
                '}';
    }
}
