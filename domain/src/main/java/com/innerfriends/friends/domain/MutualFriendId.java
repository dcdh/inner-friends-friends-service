package com.innerfriends.friends.domain;

import java.util.Objects;

public final class MutualFriendId implements Pseudo {

    private final PseudoId pseudoId;

    public MutualFriendId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public MutualFriendId(final Pseudo pseudo) {
        this(Objects.requireNonNull(pseudo)
                .pseudoId());
    }

    public MutualFriendId(final String pseudo) {
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
        if (!(o instanceof MutualFriendId)) return false;
        final MutualFriendId that = (MutualFriendId) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "MutualFriend{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
