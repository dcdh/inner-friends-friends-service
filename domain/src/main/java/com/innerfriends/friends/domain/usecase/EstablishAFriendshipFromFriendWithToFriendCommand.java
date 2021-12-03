package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.ToFriendId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class EstablishAFriendshipFromFriendWithToFriendCommand implements UseCaseCommand<FromFriendId> {

    private final FromFriendId fromFriendId;

    private final ToFriendId toFriendId;

    public EstablishAFriendshipFromFriendWithToFriendCommand(final FromFriendId fromFriendId, final ToFriendId toFriendId) {
        this.fromFriendId = Objects.requireNonNull(fromFriendId);
        this.toFriendId = Objects.requireNonNull(toFriendId);
    }

    public FromFriendId fromFriendId() {
        return fromFriendId;
    }

    public ToFriendId toFriendId() {
        return toFriendId;
    }

    @Override
    public FromFriendId identifier() {
        return fromFriendId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof EstablishAFriendshipFromFriendWithToFriendCommand)) return false;
        final EstablishAFriendshipFromFriendWithToFriendCommand that = (EstablishAFriendshipFromFriendWithToFriendCommand) o;
        return Objects.equals(fromFriendId, that.fromFriendId) &&
                Objects.equals(toFriendId, that.toFriendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromFriendId, toFriendId);
    }
}
