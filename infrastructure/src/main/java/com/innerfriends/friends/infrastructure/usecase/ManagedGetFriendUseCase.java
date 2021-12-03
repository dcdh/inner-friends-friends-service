package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.GetFriendCommand;
import com.innerfriends.friends.domain.usecase.GetFriendUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedGetFriendUseCase implements UseCase<Friend, GetFriendCommand> {

    private final GetFriendUseCase getFriendUseCase;

    public ManagedGetFriendUseCase(final GetFriendUseCase getFriendUseCase) {
        this.getFriendUseCase = Objects.requireNonNull(getFriendUseCase);
    }

    @Transactional
    @Override
    public Friend execute(final GetFriendCommand command) {
        return getFriendUseCase.execute(command);
    }

}
