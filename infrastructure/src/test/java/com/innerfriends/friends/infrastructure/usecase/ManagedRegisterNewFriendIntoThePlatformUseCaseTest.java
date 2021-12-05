package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.NewPseudoId;
import com.innerfriends.friends.domain.usecase.RegisterNewFriendIntoThePlatformCommand;
import com.innerfriends.friends.domain.usecase.RegisterNewFriendIntoThePlatformUseCase;
import com.innerfriends.friends.infrastructure.InstantProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class ManagedRegisterNewFriendIntoThePlatformUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedRegisterNewFriendIntoThePlatformUseCase managedRegisterNewFriendIntoThePlatformUseCase;

    @InjectMock
    RegisterNewFriendIntoThePlatformUseCase registerNewFriendIntoThePlatformUseCase;

    @InjectMock
    InstantProvider instantProvider;

    @Test
    public void should_register_a_new_friend_into_the_platform() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        final RegisterNewFriendIntoThePlatformCommand registerNewFriendIntoThePlatformCommand
                = new RegisterNewFriendIntoThePlatformCommand(new NewPseudoId("Mario"));
        doReturn(mario).when(registerNewFriendIntoThePlatformUseCase).execute(registerNewFriendIntoThePlatformCommand);
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When && Then
        assertThat(managedRegisterNewFriendIntoThePlatformUseCase.execute(registerNewFriendIntoThePlatformCommand))
                .isEqualTo(mario);
        assertThat(((Number) entityManager.createNativeQuery(COUNT_OUTBOX_EVENT_FOR_AGGREGATE_ID_SQL)
                .setParameter(1, "Mario")
                .getSingleResult()).longValue()).isEqualTo(1l);
        assertThat(entityManager.createNativeQuery(GET_TYPE_BY_AGGREGATE_ID)
                .setParameter(1, "Mario")
                .getSingleResult())
                .isEqualTo("NewFriendRegisteredIntoThePlatform");
    }

}
