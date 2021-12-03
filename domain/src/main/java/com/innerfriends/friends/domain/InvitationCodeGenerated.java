package com.innerfriends.friends.domain;

import java.util.Objects;

public final class InvitationCodeGenerated {

    private final FromFriendId fromFriendId;
    private final InvitationCode invitationCode;
    private final GeneratedAt generatedAt;

    public InvitationCodeGenerated(final FromFriendId fromFriendId,
                                   final InvitationCode invitationCode,
                                   final GeneratedAt generatedAt) {
        this.fromFriendId = Objects.requireNonNull(fromFriendId);
        this.invitationCode = Objects.requireNonNull(invitationCode);
        this.generatedAt = Objects.requireNonNull(generatedAt);
    }

    public FromFriendId fromFriendId() {
        return fromFriendId;
    }

    public InvitationCode invitationCode() {
        return invitationCode;
    }

    public GeneratedAt generatedAt() {
        return generatedAt;
    }

    public final Boolean isValid(final LocalDateTimeProvider localDateTimeProvider) {
        return localDateTimeProvider.now().isBefore(generatedAt.at().plusMinutes(15l));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitationCodeGenerated)) return false;
        final InvitationCodeGenerated that = (InvitationCodeGenerated) o;
        return Objects.equals(fromFriendId, that.fromFriendId) &&
                Objects.equals(invitationCode, that.invitationCode) &&
                Objects.equals(generatedAt, that.generatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromFriendId, invitationCode, generatedAt);
    }

    @Override
    public String toString() {
        return "InvitationCodeGenerated{" +
                "fromFriendId=" + fromFriendId +
                ", invitationCode=" + invitationCode +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
