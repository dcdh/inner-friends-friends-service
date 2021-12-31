package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.FriendOfFriend;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;

@RegisterForReflection
public final class FriendOfFriendDTO {

    public final String friendOfFriendId;
    public final String bio;
    public final Long version;
    public final Boolean alreadyInFriendship;

    public FriendOfFriendDTO(final FriendOfFriend friendOfFriend) {
        this.friendOfFriendId = friendOfFriend.friendOfFriendId().pseudo();
        this.bio = friendOfFriend.bio().content();
        this.version = friendOfFriend.version().value();
        this.alreadyInFriendship = friendOfFriend.alreadyInFriendship();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendOfFriendDTO)) return false;
        final FriendOfFriendDTO that = (FriendOfFriendDTO) o;
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
        return "FriendOfFriendDTO{" +
                "friendOfFriendId='" + friendOfFriendId + '\'' +
                ", bio='" + bio + '\'' +
                ", version=" + version +
                ", alreadyInFriendship=" + alreadyInFriendship +
                '}';
    }
}
