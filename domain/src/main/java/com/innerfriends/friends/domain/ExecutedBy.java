package com.innerfriends.friends.domain;

import java.util.Objects;

public final class ExecutedBy implements Pseudo {

    private final PseudoId pseudoId;

    public ExecutedBy(final PseudoId pseudoId) {
        this.pseudoId = Objects.requireNonNull(pseudoId);
    }

    public ExecutedBy(final Pseudo pseudo) {
        this(Objects.requireNonNull(pseudo)
                .pseudoId());
    }

    public ExecutedBy(final String pseudo) {
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
        if (!(o instanceof ExecutedBy)) return false;
        final ExecutedBy that = (ExecutedBy) o;
        return Objects.equals(pseudoId, that.pseudoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudoId);
    }

    @Override
    public String toString() {
        return "ExecutedBy{" +
                "pseudoId=" + pseudoId +
                '}';
    }
}
