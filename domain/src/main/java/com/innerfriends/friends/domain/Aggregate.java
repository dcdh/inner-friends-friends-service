package com.innerfriends.friends.domain;

import java.util.Objects;
import java.util.concurrent.Callable;

public abstract class Aggregate {

    private Version version;

    public Aggregate(final Version version) {
        this.version = version;
    }

    protected void apply(final Callable apply) {
        try {
            apply.call();
        } catch (final Exception exception) {
            throw new FailedToMutateAggregateException(exception);
        }
        this.version = this.version.increment();
    }

    public Version version() {
        return version;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Aggregate)) return false;
        final Aggregate aggregate = (Aggregate) o;
        return Objects.equals(version, aggregate.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(version);
    }

    @Override
    public String toString() {
        return "Aggregate{" +
                "version=" + version +
                '}';
    }
}
