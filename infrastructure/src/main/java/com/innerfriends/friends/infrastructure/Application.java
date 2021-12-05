package com.innerfriends.friends.infrastructure;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Application {

    @Produces
    @ApplicationScoped
    public EstablishAFriendshipFromFriendWithToFriendUseCase establishAFriendshipFromFriendWithToFriendUseCaseProducer(final FriendRepository friendRepository) {
        return new EstablishAFriendshipFromFriendWithToFriendUseCase(friendRepository);
    }

    @Produces
    @ApplicationScoped
    public EstablishAFriendshipToFriendWithFromFriendUseCase establishAFriendshipToFriendWithFromFriendUseCaseProducer(final InvitationCodeGeneratedRepository invitationCodeGeneratedRepository,
                                                                                                                       final FriendRepository friendRepository,
                                                                                                                       final LocalDateTimeProvider localDateTimeProvider) {
        return new EstablishAFriendshipToFriendWithFromFriendUseCase(invitationCodeGeneratedRepository, friendRepository, localDateTimeProvider);
    }

    @Produces
    @ApplicationScoped
    public GenerateInvitationCodeUseCase generateInvitationCodeUseCaseProducer(final InvitationCodeGeneratedRepository invitationCodeGeneratedRepository,
                                                                               final InvitationCodeGenerator invitationCodeGenerator,
                                                                               final GeneratedAtProvider generatedAtProvider) {
        return new GenerateInvitationCodeUseCase(invitationCodeGeneratedRepository, invitationCodeGenerator, generatedAtProvider);
    }

    @Produces
    @ApplicationScoped
    public GetFriendUseCase getFriendUseCaseProducer(final FriendRepository friendRepository) {
        return new GetFriendUseCase(friendRepository);
    }

    @Produces
    @ApplicationScoped
    public GetFriendOfFriendUseCase getFriendOfFriendUseCaseProducer(final FriendRepository friendRepository) {
        return new GetFriendOfFriendUseCase(friendRepository);
    }

    @Produces
    @ApplicationScoped
    public GetInFriendshipWithUseCase getInFriendshipWithUseCaseProducer(final FriendRepository friendRepository) {
        return new GetInFriendshipWithUseCase(friendRepository);
    }

    @Produces
    @ApplicationScoped
    public GetInFriendshipWithMutualFriendsUseCase getInFriendshipWithMutualFriendsUseCaseProducer(final MutualFriendsRepository mutualFriendsRepository) {
        return new GetInFriendshipWithMutualFriendsUseCase(mutualFriendsRepository);
    }

    @Produces
    @ApplicationScoped
    public GetFriendsOfFriendMutualFriendsUseCase getFriendsOfFriendMutualFriendsUseCaseProducer(final MutualFriendsRepository mutualFriendsRepository) {
        return new GetFriendsOfFriendMutualFriendsUseCase(mutualFriendsRepository);
    }

    @Produces
    @ApplicationScoped
    public RegisterNewFriendIntoThePlatformUseCase registerNewFriendIntoThePlatformUseCaseProducer(final FriendRepository friendRepository) {
        return new RegisterNewFriendIntoThePlatformUseCase(friendRepository);
    }

    @Produces
    @ApplicationScoped
    public WriteBioUseCase writeBioUseCaseProducer(final FriendRepository friendRepository) {
        return new WriteBioUseCase(friendRepository);
    }

    @Produces
    @ApplicationScoped
    public GeneratedAtProvider generatedAtProvider() {
        return new GeneratedAtProvider() {
            @Override
            public GeneratedAt now() {
                return new GeneratedAt(LocalDateTime.now(ZoneOffset.UTC));
            }
        };
    }

    @Produces
    @ApplicationScoped
    public LocalDateTimeProvider localDateTimeProvider() {
        return new LocalDateTimeProvider() {
            @Override
            public LocalDateTime now() {
                return LocalDateTime.now(ZoneOffset.UTC);
            }
        };
    }

    @Produces
    @ApplicationScoped
    public InvitationCodeGenerator invitationCodeGenerator() {
        return new InvitationCodeGenerator() {
            @Override
            public InvitationCode generate(final FromFriendId fromFriendId) {
                return new InvitationCode(UUID.randomUUID());
            }
        };
    }

}
