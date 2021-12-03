package com.innerfriends.friends.infrastructure.postgres;

import com.innerfriends.friends.domain.InvitationCode;
import com.innerfriends.friends.domain.InvitationCodeGenerated;
import com.innerfriends.friends.domain.InvitationCodeGeneratedRepository;
import com.innerfriends.friends.domain.UnknownInvitationCodeException;
import com.innerfriends.friends.infrastructure.opentelemetry.NewSpan;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class PostgresInvitationCodeGeneratedRepository implements InvitationCodeGeneratedRepository {

    private final EntityManager entityManager;

    public PostgresInvitationCodeGeneratedRepository(final EntityManager entityManager) {
        this.entityManager = Objects.requireNonNull(entityManager);
    }

    @NewSpan
    @Transactional(value = Transactional.TxType.MANDATORY)
    @Override
    public void store(final InvitationCodeGenerated invitationCodeGenerated) {
        final InvitationCodeGeneratedEntity invitationCodeGeneratedEntity = new InvitationCodeGeneratedEntity(invitationCodeGenerated);
        this.entityManager.persist(invitationCodeGeneratedEntity);
    }

    @NewSpan
    @Transactional(value = Transactional.TxType.MANDATORY, dontRollbackOn = {UnknownInvitationCodeException.class})
    @Override
    public InvitationCodeGenerated get(final InvitationCode invitationCode) throws UnknownInvitationCodeException {
        return Optional.ofNullable(entityManager.find(InvitationCodeGeneratedEntity.class, invitationCode.code(), LockModeType.PESSIMISTIC_WRITE))
                .map(InvitationCodeGeneratedEntity::toInvitationCodeGenerated)
                .orElseThrow(() -> new UnknownInvitationCodeException());
    }
}
