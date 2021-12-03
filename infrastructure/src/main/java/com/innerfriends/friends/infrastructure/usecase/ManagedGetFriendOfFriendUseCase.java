package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.FriendOfFriend;
import com.innerfriends.friends.domain.UseCase;
import com.innerfriends.friends.domain.usecase.GetFriendOfFriendCommand;
import com.innerfriends.friends.domain.usecase.GetFriendOfFriendUseCase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class ManagedGetFriendOfFriendUseCase implements UseCase<FriendOfFriend, GetFriendOfFriendCommand> {

    private GetFriendOfFriendUseCase getFriendOfFriendUseCase;

    public ManagedGetFriendOfFriendUseCase(final GetFriendOfFriendUseCase getFriendOfFriendUseCase) {
        this.getFriendOfFriendUseCase = Objects.requireNonNull(getFriendOfFriendUseCase);
    }

    @Transactional
    @Override
    public FriendOfFriend execute(final GetFriendOfFriendCommand command) {
        return getFriendOfFriendUseCase.execute(command);
    }

}
