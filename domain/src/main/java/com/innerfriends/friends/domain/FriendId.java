package com.innerfriends.friends.domain;

import java.util.Objects;

public final class FriendId implements Pseudo {

    private final PseudoId pseudoId;

    public FriendId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public FriendId(final Pseudo pseudo) {
        this(Objects.requireNonNull(pseudo)
                .pseudoId());
    }

    public FriendId(final String pseudo) {
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
        if (!(o instanceof FriendId)) return false;
        final FriendId friendId = (FriendId) o;
        return Objects.equals(pseudoId, friendId.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "FriendId{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
