package com.innerfriends.friends.domain;

import java.util.List;

public interface MutualFriendsRepository {

    List<MutualFriendId> getMutualFriends(FriendId friendId, InFriendshipWithId inFriendshipWithId);

    List<MutualFriendId> getMutualFriends(FriendId friendId, InFriendshipWithId inFriendshipWithId, FriendOfFriendId friendOfFriendId);

}
