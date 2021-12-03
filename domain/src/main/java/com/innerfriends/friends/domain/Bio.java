package com.innerfriends.friends.domain;

import java.util.Objects;

public final class Bio {

    private final String content;

    public Bio() {
        this("");
    }

    public Bio(final String content) {
        this.content = Objects.requireNonNull(content);
        if (content.length() >= 500) {
            throw new IllegalStateException();
        }
    }

    public String content() {
        return content;
    }

    public boolean hasBio() {
        return content.length() > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Bio)) return false;
        final Bio bio1 = (Bio) o;
        return Objects.equals(content, bio1.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
