package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.infrastructure.postgres.FriendEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class RegisterDamDamDeoUseCaseTest extends ManagedUseCaseTest {

    @Inject
    EntityManager entityManager;

    @Test
    public void should_DamDamDeo_be_registered() {
        assertThat(entityManager.find(FriendEntity.class, "DamDamDeo")).isNotNull();
    }

}
