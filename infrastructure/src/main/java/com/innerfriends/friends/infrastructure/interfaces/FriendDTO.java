package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendOfFriendId;
import com.innerfriends.friends.domain.InFriendshipWithId;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RegisterForReflection
public final class FriendDTO {

    public final String pseudo;
    public final String bio;
    public final List<String> friends;
    public final Integer nbOfFriends;
    public final Long version;

    public FriendDTO(final Friend friend) {
        this.pseudo = friend.friendId().pseudo();
        this.bio = friend.bio().content();
        this.friends = friend.inFriendshipsWith().stream()
                .map(InFriendshipWithId::pseudo)
                .collect(Collectors.toList());
        this.nbOfFriends = friend.nbOfFriends();
        this.version = friend.version().value();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendDTO)) return false;
        final FriendDTO friendDTO = (FriendDTO) o;
        return Objects.equals(pseudo, friendDTO.pseudo) &&
                Objects.equals(bio, friendDTO.bio) &&
                Objects.equals(friends, friendDTO.friends) &&
                Objects.equals(nbOfFriends, friendDTO.nbOfFriends) &&
                Objects.equals(version, friendDTO.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudo, bio, friends, nbOfFriends, version);
    }

    @Override
    public String toString() {
        return "FriendDTO{" +
                "pseudo='" + pseudo + '\'' +
                ", bio='" + bio + '\'' +
                ", friends=" + friends +
                ", nbOfFriends=" + nbOfFriends +
                ", version=" + version +
                '}';
    }
}
