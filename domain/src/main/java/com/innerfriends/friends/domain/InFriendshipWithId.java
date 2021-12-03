package com.innerfriends.friends.domain;

import java.util.Objects;

public final class InFriendshipWithId implements Pseudo {

    private final PseudoId pseudoId;

    public InFriendshipWithId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public InFriendshipWithId(final Pseudo pseudo) {
        this(Objects.requireNonNull(pseudo)
                .pseudoId());
    }

    public InFriendshipWithId(final String pseudo) {
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
        if (!(o instanceof InFriendshipWithId)) return false;
        final InFriendshipWithId that = (InFriendshipWithId) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "InFriendshipWithId{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
