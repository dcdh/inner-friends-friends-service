package com.innerfriends.friends.domain;

import java.util.Objects;

public final class FriendOfFriendId implements Pseudo {

    private final PseudoId pseudoId;

    public FriendOfFriendId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public FriendOfFriendId(final Pseudo pseudo) {
        this(Objects.requireNonNull(pseudo)
                .pseudoId());
    }

    public FriendOfFriendId(final String pseudo) {
        this(new PseudoId(pseudo));
    }

    @Override
    public PseudoId pseudoId() {
        return pseudoId;
    }

    public String pseudo() {
        return pseudoId.pseudo();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendOfFriendId)) return false;
        final FriendOfFriendId that = (FriendOfFriendId) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "FriendOfFriendId{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
