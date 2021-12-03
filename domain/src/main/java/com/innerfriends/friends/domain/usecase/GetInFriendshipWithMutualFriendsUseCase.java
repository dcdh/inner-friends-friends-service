package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.MutualFriendId;
import com.innerfriends.friends.domain.InFriendshipWithMutualFriends;
import com.innerfriends.friends.domain.MutualFriendsRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.List;
import java.util.Objects;

public class GetInFriendshipWithMutualFriendsUseCase implements UseCase<InFriendshipWithMutualFriends, GetInFriendshipWithMutualFriendsCommand> {

    private final MutualFriendsRepository mutualFriendsRepository;

    public GetInFriendshipWithMutualFriendsUseCase(final MutualFriendsRepository mutualFriendsRepository) {
        this.mutualFriendsRepository = Objects.requireNonNull(mutualFriendsRepository);
    }

    @Override
    public InFriendshipWithMutualFriends execute(final GetInFriendshipWithMutualFriendsCommand command) {
        final List<MutualFriendId> mutualFriendsId = mutualFriendsRepository.getMutualFriends(command.fromFriendId(), command.toFriendId());
        return new InFriendshipWithMutualFriends(command.fromFriendId(), command.toFriendId(), mutualFriendsId);
    }
}
