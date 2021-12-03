package com.innerfriends.friends.domain;

import java.util.Objects;

public final class PseudoId {

    private final String pseudo;

    public PseudoId(final String pseudo) {
        this.pseudo = Objects.requireNonNull(pseudo);
    }

    public String pseudo() {
        return pseudo;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof PseudoId)) return false;
        final PseudoId pseudoId = (PseudoId) o;
        return Objects.equals(pseudo, pseudoId.pseudo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pseudo);
    }

    @Override
    public String toString() {
        return "PseudoId{" +
                "pseudo='" + pseudo + '\'' +
                '}';
    }
}
