package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.FriendsOfFriendMutualFriends;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.GetFriendsOfFriendMutualFriendsCommand;
import com.innerfriends.friends.domain.usecase.GetFriendsOfFriendMutualFriendsUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedGetFriendsOfFriendMutualFriendsUseCase implements UseCase<FriendsOfFriendMutualFriends, GetFriendsOfFriendMutualFriendsCommand> {

    private GetFriendsOfFriendMutualFriendsUseCase getFriendsOfFriendMutualFriendsUseCase;

    public ManagedGetFriendsOfFriendMutualFriendsUseCase(final GetFriendsOfFriendMutualFriendsUseCase getFriendsOfFriendMutualFriendsUseCase) {
        this.getFriendsOfFriendMutualFriendsUseCase = Objects.requireNonNull(getFriendsOfFriendMutualFriendsUseCase);
    }

    @Transactional
    @Override
    public FriendsOfFriendMutualFriends execute(final GetFriendsOfFriendMutualFriendsCommand command) {
        return getFriendsOfFriendMutualFriendsUseCase.execute(command);
    }

}
