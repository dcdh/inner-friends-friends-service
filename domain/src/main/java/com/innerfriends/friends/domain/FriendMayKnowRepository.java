package com.innerfriends.friends.domain;

import java.util.List;

public interface FriendMayKnowRepository {

    List<FriendMayKnowId> mayKnow(FriendId friendId, Long nbOf);

    FriendMayKnow get(FriendId friendId, FriendMayKnowId friendMayKnowId);

}
