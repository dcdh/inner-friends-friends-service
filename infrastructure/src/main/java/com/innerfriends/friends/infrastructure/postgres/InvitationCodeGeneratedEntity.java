package com.innerfriends.friends.infrastructure.postgres;

import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.GeneratedAt;
import com.innerfriends.friends.domain.InvitationCode;
import com.innerfriends.friends.domain.InvitationCodeGenerated;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "T_INVITATION_CODE_GENERATED"
)
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class InvitationCodeGeneratedEntity {

    @Id
    @NotNull
    public UUID invitationCode;

    @NotNull
    public String fromFriendId;

    // https://jdbc.postgresql.org/documentation/head/8-date-time.html
    @NotNull
    public LocalDateTime generatedAt;

    public InvitationCodeGeneratedEntity() {}

    public InvitationCodeGeneratedEntity(final InvitationCodeGenerated invitationCodeGenerated) {
        this(invitationCodeGenerated.invitationCode().code(),
                invitationCodeGenerated.fromFriendId().pseudo(),
                invitationCodeGenerated.generatedAt().at());
    }

    public InvitationCodeGeneratedEntity(final UUID invitationCode,
                                         final String fromFriendId,
                                         final LocalDateTime generatedAt) {
        this.invitationCode = Objects.requireNonNull(invitationCode);
        this.fromFriendId = Objects.requireNonNull(fromFriendId);
        this.generatedAt = Objects.requireNonNull(generatedAt);
    }

    public InvitationCodeGenerated toInvitationCodeGenerated() {
        return new InvitationCodeGenerated(
                new FromFriendId(fromFriendId),
                new InvitationCode(invitationCode),
                new GeneratedAt(generatedAt));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof InvitationCodeGeneratedEntity)) return false;
        final InvitationCodeGeneratedEntity that = (InvitationCodeGeneratedEntity) o;
        return Objects.equals(invitationCode, that.invitationCode) &&
                Objects.equals(fromFriendId, that.fromFriendId) &&
                Objects.equals(generatedAt, that.generatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invitationCode, fromFriendId, generatedAt);
    }
}
