package com.innerfriends.friends.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public final class GeneratedAt {

    private final LocalDateTime at;

    public GeneratedAt(final LocalDateTime at) {
        this.at = Objects.requireNonNull(at);
    }

    public LocalDateTime at() {
        return at;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneratedAt)) return false;
        final GeneratedAt that = (GeneratedAt) o;
        return Objects.equals(at, that.at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(at);
    }

    @Override
    public String toString() {
        return "GeneratedAt{" +
                "at=" + at +
                '}';
    }
}
