package com.innerfriends.friends.domain;

import java.util.Objects;

public final class NewPseudoId implements Pseudo {

    private final PseudoId pseudoId;

    public NewPseudoId(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public NewPseudoId(final Pseudo pseudo) {
        this(Objects.requireNonNull(pseudo)
                .pseudoId());
    }

    public NewPseudoId(final String pseudo) {
        this(new PseudoId(pseudo));
    }

    @Override
    public PseudoId pseudoId() {
        return pseudoId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof NewPseudoId)) return false;
        final NewPseudoId that = (NewPseudoId) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "NewPseudoId{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
