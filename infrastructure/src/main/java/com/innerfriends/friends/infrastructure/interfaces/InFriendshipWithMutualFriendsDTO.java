package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.MutualFriendId;
import com.innerfriends.friends.domain.InFriendshipWithMutualFriends;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RegisterForReflection
public final class InFriendshipWithMutualFriendsDTO {

    public final String friendId;
    public final String inFriendshipWithId;

    public final List<String> mutualFriendsId;
    public final Integer nbOfMutualFriends;

    public InFriendshipWithMutualFriendsDTO(final InFriendshipWithMutualFriends inFriendshipWithMutualFriends) {
        this.friendId = inFriendshipWithMutualFriends.friendId().pseudo();
        this.inFriendshipWithId = inFriendshipWithMutualFriends.inFriendshipWithId().pseudo();
        this.mutualFriendsId = inFriendshipWithMutualFriends.mutualFriendsId()
                .stream().map(MutualFriendId::pseudo).collect(Collectors.toList());
        this.nbOfMutualFriends = inFriendshipWithMutualFriends.nbOfMutualFriends();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InFriendshipWithMutualFriendsDTO)) return false;
        final InFriendshipWithMutualFriendsDTO that = (InFriendshipWithMutualFriendsDTO) o;
        return Objects.equals(friendId, that.friendId) &&
                Objects.equals(inFriendshipWithId, that.inFriendshipWithId) &&
                Objects.equals(mutualFriendsId, that.mutualFriendsId) &&
                Objects.equals(nbOfMutualFriends, that.nbOfMutualFriends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friendId, inFriendshipWithId, mutualFriendsId, nbOfMutualFriends);
    }

    @Override
    public String toString() {
        return "InFriendshipWithMutualFriendsDTO{" +
                "friendId='" + friendId + '\'' +
                ", inFriendshipWithId='" + inFriendshipWithId + '\'' +
                ", mutualFriendsId=" + mutualFriendsId +
                ", nbOfMutualFriends=" + nbOfMutualFriends +
                '}';
    }
}
