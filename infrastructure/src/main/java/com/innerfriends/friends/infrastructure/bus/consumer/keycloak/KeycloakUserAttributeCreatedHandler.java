package com.innerfriends.friends.infrastructure.bus.consumer.keycloak;

import com.innerfriends.friends.domain.NewPseudoId;
import com.innerfriends.friends.domain.usecase.RegisterANewFriendIntoThePlatformCommand;
import com.innerfriends.friends.infrastructure.postgres.FriendEntity;
import com.innerfriends.friends.infrastructure.usecase.ManagedRegisterANewFriendIntoThePlatformUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Objects;

@ApplicationScoped
public class KeycloakUserAttributeCreatedHandler {

    private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserAttributeCreatedHandler.class);

    private final EntityManager entityManager;
    private final ManagedRegisterANewFriendIntoThePlatformUseCase managedRegisterANewFriendIntoThePlatformUseCase;

    public KeycloakUserAttributeCreatedHandler(final EntityManager entityManager,
                                               final ManagedRegisterANewFriendIntoThePlatformUseCase managedRegisterANewFriendIntoThePlatformUseCase) {
        this.entityManager = Objects.requireNonNull(entityManager);
        this.managedRegisterANewFriendIntoThePlatformUseCase = Objects.requireNonNull(managedRegisterANewFriendIntoThePlatformUseCase);
    }

    @Transactional
    public void onUserAttributeCreated(final String name, final String value) {
        LOG.info("Received created attribute '{}' with value '{}'", name, value);
        if ("friendId".equals(name)) {
            if (entityManager.find(FriendEntity.class, value) == null) {
                managedRegisterANewFriendIntoThePlatformUseCase.execute(new RegisterANewFriendIntoThePlatformCommand(new NewPseudoId(value)));
            }
        } else {
            LOG.info("Unknown attribute name created '{}' - ignored.", name);
        }
    }

}
