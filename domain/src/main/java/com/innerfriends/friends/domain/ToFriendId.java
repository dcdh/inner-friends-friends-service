package com.innerfriends.friends.domain;

import java.util.Objects;

public final class ToFriendId implements Pseudo {

    private final PseudoId pseudoId;

    public ToFriendId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public ToFriendId(final String pseudo) {
        this(new PseudoId(pseudo));
    }

    @Override
    public PseudoId pseudoId() {
        return pseudoId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ToFriendId)) return false;
        final ToFriendId that = (ToFriendId) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "ToFriendId{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
