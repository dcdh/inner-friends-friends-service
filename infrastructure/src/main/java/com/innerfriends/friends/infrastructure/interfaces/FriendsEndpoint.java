package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.domain.usecase.*;
import com.innerfriends.friends.infrastructure.usecase.*;
import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.Claim;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Objects;
import java.util.UUID;

@Path("/friends")
@Authenticated
@RequestScoped
public class FriendsEndpoint {

    private final String friendId;
    private final ManagedGetFriendUseCase managedGetFriendUseCase;
    private final ManagedGetInFriendshipWithUseCase managedGetInFriendshipWithUseCase;
    private final ManagedGenerateInvitationCodeUseCase managedGenerateInvitationCodeUseCase;
    private final ManagedWriteBioUseCase managedWriteBioUseCase;
    private final ManagedEstablishAFriendshipToFriendWithFromFriendUseCase managedEstablishAFriendshipToFriendWithFromFriendUseCase;
    private final ManagedGetFriendOfFriendUseCase managedGetFriendOfFriendUseCase;
    private final ManagedGetInFriendshipWithMutualFriendsUseCase managedGetInFriendshipWithMutualFriendsUseCase;
    private final ManagedGetFriendsOfFriendMutualFriendsUseCase managedGetFriendsOfFriendMutualFriendsUseCase;

    public FriendsEndpoint(@Claim("friendId") final String friendId,
                           final ManagedGetFriendUseCase managedGetFriendUseCase,
                           final ManagedGetInFriendshipWithUseCase managedGetInFriendshipWithUseCase,
                           final ManagedGenerateInvitationCodeUseCase managedGenerateInvitationCodeUseCase,
                           final ManagedWriteBioUseCase managedWriteBioUseCase,
                           final ManagedEstablishAFriendshipToFriendWithFromFriendUseCase managedEstablishAFriendshipToFriendWithFromFriendUseCase,
                           final ManagedGetFriendOfFriendUseCase managedGetFriendOfFriendUseCase,
                           final ManagedGetInFriendshipWithMutualFriendsUseCase managedGetInFriendshipWithMutualFriendsUseCase,
                           final ManagedGetFriendsOfFriendMutualFriendsUseCase managedGetFriendsOfFriendMutualFriendsUseCase) {
        this.friendId = Objects.requireNonNull(friendId);
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
    @RolesAllowed({"friend", "admin"})
    @Path("/{friendId}")
    public FriendDTO getFriend(@PathParam("friendId") final String friendId) {
        return new FriendDTO(managedGetFriendUseCase.execute(new GetFriendCommand(new FriendId(friendId))));
    }

    @POST
    @RolesAllowed("friend")
    @Path("/generateInvitationCode")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public InvitationCodeDTO generateInvitationCode() {
        return new InvitationCodeDTO(managedGenerateInvitationCodeUseCase.execute(new GenerateInvitationCodeCommand(new FromFriendId(friendId))).invitationCode());
    }

    @POST
    @RolesAllowed("friend")
    @Path("/writeBio")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public FriendDTO writeBio(@FormParam("bio") final String bio) {
        return new FriendDTO(managedWriteBioUseCase.execute(new WriteBioCommand(new FriendId(friendId), new Bio(bio), new ExecutedBy(friendId))));
    }

    @POST
    @RolesAllowed("friend")
    @Path("/establishAFriendship")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public FriendDTO establishAFriendship(@FormParam("invitationCode") final String invitationCode) {
        return new FriendDTO(managedEstablishAFriendshipToFriendWithFromFriendUseCase.execute(new EstablishAFriendshipToFriendWithFromFriendCommand(
                new ToFriendId(friendId), new InvitationCode(UUID.fromString(invitationCode)), new ExecutedBy(friendId))));
    }

    @GET
    @RolesAllowed({"friend", "admin"})
    @Path("/{friendId}/inFriendshipsWith/{inFriendshipWithId}")
    public InFriendshipWithDTO getInFriendshipsWith(@PathParam("friendId") final String friendId,
                                                    @PathParam("inFriendshipWithId") final String inFriendshipWithId) {
        // TODO if friend next check friendId equals authenticated friendId
        // TODO if admin return always true
        return new InFriendshipWithDTO(managedGetInFriendshipWithUseCase.execute(new GetInFriendshipWithCommand(
                new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId))));
    }

    @GET
    @RolesAllowed({"friend", "admin"})
    @Path("/{friendId}/inFriendshipsWith/{inFriendshipWithId}/mutualFriends")
    public InFriendshipWithMutualFriendsDTO getInFriendshipsWithMutualFriends(@PathParam("friendId") final String friendId,
                                                                              @PathParam("inFriendshipWithId") final String inFriendshipWithId) {
        // TODO if friend next check friendId equals authenticated friendId
        // TODO if admin return always true
        return new InFriendshipWithMutualFriendsDTO(managedGetInFriendshipWithMutualFriendsUseCase.execute(
                new GetInFriendshipWithMutualFriendsCommand(new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId))));
    }

    @GET
    @RolesAllowed({"friend", "admin"})
    @Path("/{friendId}/inFriendshipsWith/{inFriendshipWithId}/friendsOfFriend/{friendOfFriendId}")
    public FriendOfFriendDTO getFriendOfFriend(@PathParam("friendId") final String friendId,
                                               @PathParam("inFriendshipWithId") final String inFriendshipWithId,
                                               @PathParam("friendOfFriendId") final String friendOfFriendId) {
        // TODO if friend next check friendId equals authenticated friendId
        // TODO if admin return always true
        return new FriendOfFriendDTO(managedGetFriendOfFriendUseCase.execute(new GetFriendOfFriendCommand(
                new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId), new FriendOfFriendId(friendOfFriendId))));
    }

    @GET
    @RolesAllowed({"friend", "admin"})
    @Path("/{friendId}/inFriendshipsWith/{inFriendshipWithId}/friendsOfFriend/{friendOfFriendId}/mutualFriends")
    public FriendsOfFriendMutualFriendsDTO getFriendOfFriendMutualFriends(@PathParam("friendId") final String friendId,
                                                                          @PathParam("inFriendshipWithId") final String inFriendshipWithId,
                                                                          @PathParam("friendOfFriendId") final String friendOfFriendId) {
        // TODO if friend next check friendId equals authenticated friendId
        // TODO if admin return always true
        return new FriendsOfFriendMutualFriendsDTO(managedGetFriendsOfFriendMutualFriendsUseCase.execute(
                new GetFriendsOfFriendMutualFriendsCommand(new FriendId(friendId), new InFriendshipWithId(inFriendshipWithId),
                        new FriendOfFriendId(friendOfFriendId))));
    }

}
