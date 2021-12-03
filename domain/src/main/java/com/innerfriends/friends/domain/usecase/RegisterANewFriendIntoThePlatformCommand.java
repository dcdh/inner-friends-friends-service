package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.NewPseudoId;
import com.innerfriends.friends.domain.UseCaseCommand;

import java.util.Objects;

public final class RegisterANewFriendIntoThePlatformCommand implements UseCaseCommand<NewPseudoId> {

    private final NewPseudoId newPseudoId;

    public RegisterANewFriendIntoThePlatformCommand(final NewPseudoId newPseudoId) {
        this.newPseudoId = Objects.requireNonNull(newPseudoId);
    }

    @Override
    public NewPseudoId identifier() {
        return newPseudoId;
    }

    public NewPseudoId newPseudoId() {
        return newPseudoId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof RegisterANewFriendIntoThePlatformCommand)) return false;
        final RegisterANewFriendIntoThePlatformCommand that = (RegisterANewFriendIntoThePlatformCommand) o;
        return Objects.equals(newPseudoId, that.newPseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(newPseudoId);
    }
}
