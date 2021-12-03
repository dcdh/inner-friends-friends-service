package com.innerfriends.friends.domain;

public interface FriendRepository {

    Friend getBy(FriendId friendId) throws FriendUnknownException;

    void create(Friend friend) throws PseudoAlreadyUsedException;

    void save(Friend friend);

}
