package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.FriendOfFriendId;
import com.innerfriends.friends.domain.InFriendshipWith;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RegisterForReflection
public final class InFriendshipWithDTO {

    public final String inFriendshipWithId;
    public final List<String> friendsOfFriendId;
    public final String bio;
    public final Long version;

    public InFriendshipWithDTO(final InFriendshipWith inFriendshipWith) {
        this.inFriendshipWithId = inFriendshipWith.inFriendshipWithId().pseudo();
        this.friendsOfFriendId = inFriendshipWith.friendsOfFriendId().stream()
                .map(FriendOfFriendId::pseudo).collect(Collectors.toList());
        this.bio = inFriendshipWith.bio().content();
        this.version = inFriendshipWith.version().value();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InFriendshipWithDTO)) return false;
        final InFriendshipWithDTO that = (InFriendshipWithDTO) o;
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
        return "InFriendshipWithDTO{" +
                "inFriendshipWithId='" + inFriendshipWithId + '\'' +
                ", friendsOfFriendId=" + friendsOfFriendId +
                ", bio='" + bio + '\'' +
                ", version=" + version +
                '}';
    }
}
