package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.FriendOfFriend;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;

@RegisterForReflection
public final class FriendOfFriendDTO {

    public final String pseudo;
    public final String bio;
    public final Long version;

    public FriendOfFriendDTO(final FriendOfFriend friendOfFriend) {
        this.pseudo = friendOfFriend.friendOfFriendId().pseudo();
        this.bio = friendOfFriend.bio().content();
        this.version = friendOfFriend.version().value();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendOfFriendDTO)) return false;
        final FriendOfFriendDTO that = (FriendOfFriendDTO) o;
        return Objects.equals(pseudo, that.pseudo) &&
                Objects.equals(bio, that.bio) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudo, bio, version);
    }

    @Override
    public String toString() {
        return "FriendOfFriendDTO{" +
                "pseudo='" + pseudo + '\'' +
                ", bio='" + bio + '\'' +
                ", version=" + version +
                '}';
    }
}
