package com.innerfriends.friends.infrastructure.postgres;

import com.innerfriends.friends.domain.*;
import com.innerfriends.friends.infrastructure.opentelemetry.NewSpan;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class PostgresFriendRepository implements FriendRepository {

    private final EntityManager entityManager;

    public PostgresFriendRepository(final EntityManager entityManager) {
        this.entityManager = Objects.requireNonNull(entityManager);
    }

    @NewSpan
    @Transactional(value = Transactional.TxType.MANDATORY, dontRollbackOn = {FriendUnknownException.class})
    @Override
    public Friend getBy(final FriendId friendId) throws FriendUnknownException {
        return Optional.ofNullable(entityManager.find(FriendEntity.class, friendId.pseudo(), LockModeType.PESSIMISTIC_WRITE))
                .map(FriendEntity::toFriend)
                .orElseThrow(() -> new FriendUnknownException(friendId));
    }

    @NewSpan
    @Transactional(value = Transactional.TxType.MANDATORY, dontRollbackOn = {PseudoAlreadyUsedException.class})
    @Override
    public void create(final Friend friend) throws PseudoAlreadyUsedException {
        final FriendEntity friendEntityToCreate = new FriendEntity(friend);
        if (this.entityManager.find(FriendEntity.class, friend.friendId().pseudo(), LockModeType.PESSIMISTIC_WRITE) != null) {
            throw new PseudoAlreadyUsedException(friend.friendId());
        }
        this.entityManager.persist(friendEntityToCreate);
    }

    @NewSpan
    @Transactional(value = Transactional.TxType.MANDATORY)
    @Override
    public void save(final Friend friend) {
        final FriendEntity friendEntityToSave = new FriendEntity(friend);
        this.entityManager.merge(friendEntityToSave);
    }

}
