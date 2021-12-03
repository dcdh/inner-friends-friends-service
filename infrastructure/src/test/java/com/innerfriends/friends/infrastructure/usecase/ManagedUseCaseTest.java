package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.infrastructure.postgres.RepositoryTesting;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

public abstract class ManagedUseCaseTest {

    public static final String DELETE_ALL_OUTBOX_EVENTS_SQL = "DELETE FROM public.outboxevent";
    public static final String COUNT_OUTBOX_EVENT_FOR_AGGREGATE_ID_SQL = "SELECT COUNT(*) FROM public.outboxevent WHERE aggregateid = ?1";
    public static final String GET_TYPE_BY_AGGREGATE_ID = "SELECT TYPE FROM public.outboxevent WHERE aggregateid = ?1";

    @Inject
    UserTransaction userTransaction;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @AfterEach
    public void setup() throws Exception {
        userTransaction.begin();
        entityManager.createNativeQuery(RepositoryTesting.DELETE_ALL_IN_T_FRIEND).executeUpdate();
        entityManager.createNativeQuery(RepositoryTesting.DELETE_ALL_IN_T_INVITATION_CODE_GENERATED).executeUpdate();
        entityManager.createNativeQuery(DELETE_ALL_OUTBOX_EVENTS_SQL).executeUpdate();
        userTransaction.commit();
    }

}

