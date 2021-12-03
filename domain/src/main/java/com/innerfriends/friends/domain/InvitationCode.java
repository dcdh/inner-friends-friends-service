package com.innerfriends.friends.domain;

import java.util.Objects;
import java.util.UUID;

public final class InvitationCode {

    private final UUID code;

    public InvitationCode(final UUID code) {
        this.code = Objects.requireNonNull(code);
    }

    public UUID code() {
        return code;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitationCode)) return false;
        final InvitationCode that = (InvitationCode) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "InvitationCode{" +
                "code=" + code +
                '}';
    }
}
