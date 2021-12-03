package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.InFriendshipWithMutualFriends;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.GetInFriendshipWithMutualFriendsCommand;
import com.innerfriends.friends.domain.usecase.GetInFriendshipWithMutualFriendsUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedGetInFriendshipWithMutualFriendsUseCase implements UseCase<InFriendshipWithMutualFriends, GetInFriendshipWithMutualFriendsCommand> {

    private final GetInFriendshipWithMutualFriendsUseCase getInFriendshipWithMutualFriendsUseCase;

    public ManagedGetInFriendshipWithMutualFriendsUseCase(final GetInFriendshipWithMutualFriendsUseCase getInFriendshipWithMutualFriendsUseCase) {
        this.getInFriendshipWithMutualFriendsUseCase = Objects.requireNonNull(getInFriendshipWithMutualFriendsUseCase);
    }

    @Transactional
    @Override
    public InFriendshipWithMutualFriends execute(final GetInFriendshipWithMutualFriendsCommand command) {
        return getInFriendshipWithMutualFriendsUseCase.execute(command);
    }

}
