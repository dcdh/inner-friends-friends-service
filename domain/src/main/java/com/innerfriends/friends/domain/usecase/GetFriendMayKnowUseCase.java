package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendMayKnow;
import com.innerfriends.friends.domain.FriendMayKnowRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.Objects;

public class GetFriendMayKnowUseCase implements UseCase<FriendMayKnow, GetFriendMayKnowCommand> {

    private final FriendMayKnowRepository friendMayKnowRepository;

    public GetFriendMayKnowUseCase(final FriendMayKnowRepository friendMayKnowRepository) {
        this.friendMayKnowRepository = Objects.requireNonNull(friendMayKnowRepository);
    }

    @Override
    public FriendMayKnow execute(final GetFriendMayKnowCommand command) {
        return friendMayKnowRepository.get(command.friendId(), command.friendMayKnowId());
    }

}
