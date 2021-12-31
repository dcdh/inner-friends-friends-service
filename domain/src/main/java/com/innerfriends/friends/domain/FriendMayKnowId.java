package com.innerfriends.friends.domain;

import java.util.Objects;

public final class FriendMayKnowId implements Pseudo {

    private final PseudoId pseudoId;

    public FriendMayKnowId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public FriendMayKnowId(final Pseudo pseudo) {
        this(Objects.requireNonNull(pseudo)
                .pseudoId());
    }

    public FriendMayKnowId(final String pseudo) {
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
        if (!(o instanceof FriendMayKnowId)) return false;
        final FriendMayKnowId that = (FriendMayKnowId) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "YouMayKnowId{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}