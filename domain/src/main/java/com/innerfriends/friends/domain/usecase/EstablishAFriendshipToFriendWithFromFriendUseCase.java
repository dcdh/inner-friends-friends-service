package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;

import java.util.Objects;

public class EstablishAFriendshipToFriendWithFromFriendUseCase implements UseCase<Friend, EstablishAFriendshipToFriendWithFromFriendCommand> {

    private final InvitationCodeGeneratedRepository invitationCodeGeneratedRepository;
    private final FriendRepository friendRepository;
    private final LocalDateTimeProvider localDateTimeProvider;

    public EstablishAFriendshipToFriendWithFromFriendUseCase(final InvitationCodeGeneratedRepository invitationCodeGeneratedRepository,
                                                             final FriendRepository friendRepository,
                                                             final LocalDateTimeProvider localDateTimeProvider) {
        this.invitationCodeGeneratedRepository = Objects.requireNonNull(invitationCodeGeneratedRepository);
        this.friendRepository = Objects.requireNonNull(friendRepository);
        this.localDateTimeProvider = Objects.requireNonNull(localDateTimeProvider);
    }

    @Override
    public Friend execute(final EstablishAFriendshipToFriendWithFromFriendCommand command) {
        final InvitationCodeGenerated invitationCodeGenerated = invitationCodeGeneratedRepository.get(command.invitationCode());
        if (Boolean.FALSE.equals(invitationCodeGenerated.isValid(localDateTimeProvider))) {
            throw new InvitationCodeInvalideException();
        }
        final Friend friend = friendRepository.getBy(new FriendId(command.toFriendId()));
        friend.establishAFriendship(new FriendId(invitationCodeGenerated.fromFriendId()));
        friendRepository.save(friend);
        return friend;
    }

}
