package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.FriendMayKnow;
import com.innerfriends.friends.domain.MutualFriendId;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.stream.Collectors;

@RegisterForReflection
public final class FriendMayKnowDTO {

    public final String friendMayKnowId;
    public final String bio;
    public final Long version;
    public final List<String> mutualFriendsId;

    public FriendMayKnowDTO(final FriendMayKnow friendMayKnow) {
        this.friendMayKnowId = friendMayKnow.youMayKnowId().pseudo();
        this.bio = friendMayKnow.bio().content();
        this.version = friendMayKnow.version().value();
        this.mutualFriendsId = friendMayKnow.mutualFriendsId()
                .stream().map(MutualFriendId::pseudo).collect(Collectors.toList());
    }

}
