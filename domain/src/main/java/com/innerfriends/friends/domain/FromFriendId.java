package com.innerfriends.friends.domain;

import java.util.Objects;

public final class FromFriendId implements Pseudo {

    private final PseudoId pseudoId;

    public FromFriendId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public FromFriendId(final String pseudo) {
        this(new PseudoId(pseudo));
    }

    @Override
    public PseudoId pseudoId() {
        return pseudoId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof FromFriendId)) return false;
        final FromFriendId that = (FromFriendId) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "FromFriendId{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
