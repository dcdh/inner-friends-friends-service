package com.innerfriends.friends.infrastructure.postgres;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.concurrent.Callable;

public abstract class RepositoryTesting {

    public static final String DELETE_ALL_IN_T_FRIEND = "DELETE FROM public.T_FRIEND WHERE friendId != 'DamDamDeo'";
    public static final String DELETE_ALL_IN_T_INVITATION_CODE_GENERATED = "DELETE FROM public.T_INVITATION_CODE_GENERATED";
    public static final String DELETE_ALL_IN_T_CONSUMED_MESSAGE = "DELETE FROM public.T_CONSUMED_MESSAGE";

    @Inject
    UserTransaction userTransaction;

    @Inject
    EntityManager entityManager;

    @BeforeEach
    @AfterEach
    public void flush() throws Exception {
        runInTransaction(() ->
                entityManager.createNativeQuery(DELETE_ALL_IN_T_FRIEND).executeUpdate());
        runInTransaction(() ->
                entityManager.createNativeQuery(DELETE_ALL_IN_T_INVITATION_CODE_GENERATED).executeUpdate());
        runInTransaction(() ->
                entityManager.createNativeQuery(DELETE_ALL_IN_T_CONSUMED_MESSAGE).executeUpdate());
    }

    public <V> V runInTransaction(final Callable<V> callable) throws Exception {
        userTransaction.begin();
        try {
            return callable.call();
        } catch (final Exception exception) {
            throw exception;
        } finally {
            userTransaction.commit();
        }
    }

}
