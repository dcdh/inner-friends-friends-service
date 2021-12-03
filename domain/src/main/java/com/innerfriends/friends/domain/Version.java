package com.innerfriends.friends.domain;

import java.util.Objects;

public final class Version {

    private final Long value;

    public Version() {
        this(0l);
    }

    public Version(final Long value) {
        this.value = Objects.requireNonNull(value);
        if (value < 0) {
            throw new IllegalStateException("Must be >= 0");
        }
    }

    public Long value() {
        return value;
    }

    public synchronized Version increment() {
        return new Version(value + 1);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;
        final Version version1 = (Version) o;
        return Objects.equals(value, version1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Version{" +
                "value=" + value +
                '}';
    }
}
