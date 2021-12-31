package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FriendMayKnowId;
import com.innerfriends.friends.domain.FriendMayKnowRepository;
import com.innerfriends.friends.domain.UseCase;

import java.util.List;
import java.util.Objects;

public class ListFriendMayKnowUseCase implements UseCase<List<FriendMayKnowId>, ListFriendMayKnowCommand> {

    private final FriendMayKnowRepository friendMayKnowRepository;

    public ListFriendMayKnowUseCase(final FriendMayKnowRepository friendMayKnowRepository) {
        this.friendMayKnowRepository = Objects.requireNonNull(friendMayKnowRepository);
    }

    @Override
    public List<FriendMayKnowId> execute(final ListFriendMayKnowCommand command) {
        return friendMayKnowRepository.mayKnow(command.friendId(), command.nbOf());
    }

}
