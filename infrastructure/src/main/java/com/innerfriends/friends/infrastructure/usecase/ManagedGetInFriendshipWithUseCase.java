package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.InFriendshipWith;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.GetInFriendshipWithCommand;
import com.innerfriends.friends.domain.usecase.GetInFriendshipWithUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedGetInFriendshipWithUseCase implements UseCase<InFriendshipWith, GetInFriendshipWithCommand> {

    private final GetInFriendshipWithUseCase getInFriendshipWithUseCase;

    public ManagedGetInFriendshipWithUseCase(final GetInFriendshipWithUseCase getInFriendshipWithUseCase) {
        this.getInFriendshipWithUseCase = Objects.requireNonNull(getInFriendshipWithUseCase);
    }

    @Transactional
    @Override
    public InFriendshipWith execute(final GetInFriendshipWithCommand command) {
        return getInFriendshipWithUseCase.execute(command);
    }
}
