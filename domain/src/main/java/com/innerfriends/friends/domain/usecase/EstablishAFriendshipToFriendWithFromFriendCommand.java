package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;

import java.util.Objects;

public final class EstablishAFriendshipToFriendWithFromFriendCommand implements UseCaseCommand<ToFriendId> {

    private final ToFriendId toFriendId;

    private final InvitationCode invitationCode;

    private final ExecutedBy executedBy;

    public EstablishAFriendshipToFriendWithFromFriendCommand(final ToFriendId toFriendId,
                                                             final InvitationCode invitationCode,
                                                             final ExecutedBy executedBy) {
        this.toFriendId = Objects.requireNonNull(toFriendId);
        this.invitationCode = Objects.requireNonNull(invitationCode);
        this.executedBy = Objects.requireNonNull(executedBy);
    }

    public ToFriendId toFriendId() {
        return toFriendId;
    }

    public InvitationCode invitationCode() {
        return invitationCode;
    }

    public ExecutedBy executedBy() {
        return executedBy;
    }

    @Override
    public ToFriendId identifier() {
        return toFriendId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof EstablishAFriendshipToFriendWithFromFriendCommand)) return false;
        final EstablishAFriendshipToFriendWithFromFriendCommand that = (EstablishAFriendshipToFriendWithFromFriendCommand) o;
        return Objects.equals(toFriendId, that.toFriendId) &&
                Objects.equals(invitationCode, that.invitationCode) &&
                Objects.equals(executedBy, that.executedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toFriendId, invitationCode, executedBy);
    }

    @Override
    public String toString() {
        return "ConnectToFriendWithFromFriendCommand{" +
                "toFriendId=" + toFriendId +
                ", invitationCode=" + invitationCode +
                ", executedBy=" + executedBy +
                '}';
    }
}
