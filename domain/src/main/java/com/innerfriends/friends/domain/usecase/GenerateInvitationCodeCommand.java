package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class GenerateInvitationCodeCommand implements UseCaseCommand<FromFriendId> {

    private final FromFriendId fromFriendId;

    public GenerateInvitationCodeCommand(final FromFriendId fromFriendId) {
        this.fromFriendId = Objects.requireNonNull(fromFriendId);
    }

    @Override
    public FromFriendId identifier() {
        return fromFriendId;
    }

    public FromFriendId fromFriendId() {
        return fromFriendId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GenerateInvitationCodeCommand)) return false;
        final GenerateInvitationCodeCommand that = (GenerateInvitationCodeCommand) o;
        return Objects.equals(fromFriendId, that.fromFriendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromFriendId);
    }
}
