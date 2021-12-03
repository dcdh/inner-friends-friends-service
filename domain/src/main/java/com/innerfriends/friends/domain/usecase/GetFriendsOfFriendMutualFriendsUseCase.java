package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendsOfFriendMutualFriends;
import com.innerfriends.friends.domain.MutualFriendId;
import com.innerfriends.friends.domain.MutualFriendsRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.List;
import java.util.Objects;

public class GetFriendsOfFriendMutualFriendsUseCase implements UseCase<FriendsOfFriendMutualFriends, GetFriendsOfFriendMutualFriendsCommand> {

    private final MutualFriendsRepository mutualFriendsRepository;

    public GetFriendsOfFriendMutualFriendsUseCase(final MutualFriendsRepository mutualFriendsRepository) {
        this.mutualFriendsRepository = Objects.requireNonNull(mutualFriendsRepository);
    }

    @Override
    public FriendsOfFriendMutualFriends execute(final GetFriendsOfFriendMutualFriendsCommand command) {
        final List<MutualFriendId> mutualFriendsId = mutualFriendsRepository.getMutualFriends(command.friendId(),
                command.inFriendshipWithId(),
                command.friendOfFriendId());
        return new FriendsOfFriendMutualFriends(
                command.friendId(),
                command.inFriendshipWithId(),
                command.friendOfFriendId(),
                mutualFriendsId);
    }

}
