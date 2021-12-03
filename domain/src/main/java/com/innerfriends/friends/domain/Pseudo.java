package com.innerfriends.friends.domain;

public interface Pseudo {

    PseudoId pseudoId();

    default String pseudo() {
        return pseudoId().pseudo();
    }

}
