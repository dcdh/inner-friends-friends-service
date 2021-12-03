package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.InvitationCode;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;

@RegisterForReflection
public final class InvitationCodeDTO {

    public final String invitationCode;

    public InvitationCodeDTO(final InvitationCode invitationCode) {
        this.invitationCode = invitationCode.code().toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitationCodeDTO)) return false;
        final InvitationCodeDTO that = (InvitationCodeDTO) o;
        return Objects.equals(invitationCode, that.invitationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invitationCode);
    }
}
