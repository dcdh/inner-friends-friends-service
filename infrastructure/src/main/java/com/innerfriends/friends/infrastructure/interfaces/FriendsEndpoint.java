package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.*;
import com.innerfriends.friends.infrastructure.usecase.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Objects;
import java.util.UUID;

@Path("/")
public class FriendsEndpoint {

    private final ManagedGetFriendUseCase managedGetFriendUseCase;
    private final ManagedGetInFriendshipWithUseCase managedGetInFriendshipWithUseCase;
    private final ManagedGenerateInvitationCodeUseCase managedGenerateInvitationCodeUseCase;
    private final ManagedWriteBioUseCase managedWriteBioUseCase;
    private final ManagedEstablishAFriendshipToFriendWithFromFriendUseCase managedEstablishAFriendshipToFriendWithFromFriendUseCase;
    private final ManagedGetFriendOfFriendUseCase managedGetFriendOfFriendUseCase;
    private final ManagedGetInFriendshipWithMutualFriendsUseCase managedGetInFriendshipWithMutualFriendsUseCase;
    private final ManagedGetFriendsOfFriendMutualFriendsUseCase managedGetFriendsOfFriendMutualFriendsUseCase;

    public FriendsEndpoint(final ManagedGetFriendUseCase managedGetFriendUseCase,
                           final ManagedGetInFriendshipWithUseCase managedGetInFriendshipWithUseCase,
                           final ManagedGenerateInvitationCodeUseCase managedGenerateInvitationCodeUseCase,
                           final ManagedWriteBioUseCase managedWriteBioUseCase,
                           final ManagedEstablishAFriendshipToFriendWithFromFriendUseCase managedEstablishAFriendshipToFriendWithFromFriendUseCase,
                           final ManagedGetFriendOfFriendUseCase managedGetFriendOfFriendUseCase,
                           final ManagedGetInFriendshipWithMutualFriendsUseCase managedGetInFriendshipWithMutualFriendsUseCase,
                           final ManagedGetFriendsOfFriendMutualFriendsUseCase managedGetFriendsOfFriendMutualFriendsUseCase) {
        this.managedGetFriendUseCase = Objects.requireNonNull(managedGetFriendUseCase);
        this.managedGetInFriendshipWithUseCase = Objects.requireNonNull(managedGetInFriendshipWithUseCase);
        this.managedGenerateInvitationCodeUseCase = Objects.requireNonNull(managedGenerateInvitationCodeUseCase);
        this.managedWriteBioUseCase = Objects.requireNonNull(managedWriteBioUseCase);
        this.managedEstablishAFriendshipToFriendWithFromFriendUseCase = Objects.requireNonNull(managedEstablishAFriendshipToFriendWithFromFriendUseCase);
        this.managedGetFriendOfFriendUseCase = Objects.requireNonNull(managedGetFriendOfFriendUseCase);
        this.managedGetInFriendshipWithMutualFriendsUseCase = Objects.requireNonNull(managedGetInFriendshipWithMutualFriendsUseCase);
        this.managedGetFriendsOfFriendMutualFriendsUseCase = Objects.requireNonNull(managedGetFriendsOfFriendMutualFriendsUseCase);
    }

    @GET
    @Path("/friends/{friendId}")
    public FriendDTO getFriend(@PathParam("friendId") final String friendId) {
        return new FriendDTO(managedGetFriendUseCase.execute(new GetFriendCommand(new FriendId(friendId))));
    }

    @POST
    @Path("/friends/{fromFriendId}/generateInvitationCode")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public InvitationCodeDTO generateInvitationCode(@PathParam("fromFriendId") final String fromFriendId) {
        return new InvitationCodeDTO(managedGenerateInvitationCodeUseCase.execute(new GenerateInvitationCodeCommand(new FromFriendId(fromFriendId))).invitationCode());
    }

    @POST
    @Path("/friends/{friendId}/writeBio")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public FriendDTO writeBio(@PathParam("friendId") final String friendId,
                              @FormParam("bio") final String bio,
                              @FormParam("executedBy") final String executedBy) {
        return new FriendDTO(managedWriteBioUseCase.execute(new WriteBioCommand(new FriendId(friendId), new Bio(bio), new ExecutedBy(executedBy))));
    }

    @POST
    @Path("/friends/{toFriendId}/establishAFriendship")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public FriendDTO establishAFriendship(@PathParam("toFriendId") final String toFriendId,
                                          @FormParam("invitationCode") final String invitationCode,
                                          @FormParam("executedBy") final String executedBy) {
        return new FriendDTO(managedEstablishAFriendshipToFriendWithFromFriendUseCase.execute(new EstablishAFriendshipToFriendWithFromFriendCommand(
                new ToFriendId(toFriendId), new InvitationCode(UUID.fromString(invitationCode)), new ExecutedBy(executedBy))));
    }

    @GET
    @Path("/friends/{friendId}/inFriendshipsWith/{inFriendshipWithId}")
    public InFriendshipWithDTO getInFriendshipsWith(@PathParam("friendId") final String friendId,
                                                    @PathParam("inFriendshipWithId") final String inFriendshipWithId) {
        return new InFriendshipWithDTO(managedGetInFriendshipWithUseCase.execute(new GetInFriendshipWithCommand(
                new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId))));
    }

    @GET
    @Path("/friends/{friendId}/inFriendshipsWith/{inFriendshipWithId}/mutualFriends")
    public InFriendshipWithMutualFriendsDTO getInFriendshipsWithMutualFriends(@PathParam("friendId") final String friendId,
                                                                              @PathParam("inFriendshipWithId") final String inFriendshipWithId) {
        return new InFriendshipWithMutualFriendsDTO(managedGetInFriendshipWithMutualFriendsUseCase.execute(
                new GetInFriendshipWithMutualFriendsCommand(new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId))));
    }

    @GET
    @Path("/friends/{friendId}/inFriendshipsWith/{inFriendshipWithId}/friendsOfFriend/{friendOfFriendId}")
    public FriendOfFriendDTO getFriendOfFriend(@PathParam("friendId") final String friendId,
                                               @PathParam("inFriendshipWithId") final String inFriendshipWithId,
                                               @PathParam("friendOfFriendId") final String friendOfFriendId) {
        return new FriendOfFriendDTO(managedGetFriendOfFriendUseCase.execute(new GetFriendOfFriendCommand(
                new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId), new FriendOfFriendId(friendOfFriendId))));
    }

    @GET
    @Path("/friends/{friendId}/inFriendshipsWith/{inFriendshipWithId}/friendsOfFriend/{friendOfFriendId}/mutualFriends")
    public FriendsOfFriendMutualFriendsDTO getFriendOfFriendMutualFriends(@PathParam("friendId") final String friendId,
                                                                          @PathParam("inFriendshipWithId") final String inFriendshipWithId,
                                                                          @PathParam("friendOfFriendId") final String friendOfFriendId) {
        return new FriendsOfFriendMutualFriendsDTO(managedGetFriendsOfFriendMutualFriendsUseCase.execute(
                new GetFriendsOfFriendMutualFriendsCommand(new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId),
                        new FriendOfFriendId(friendOfFriendId))));
    }

}
