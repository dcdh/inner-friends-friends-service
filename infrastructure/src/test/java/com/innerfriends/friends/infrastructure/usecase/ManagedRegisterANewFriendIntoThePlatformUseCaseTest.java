package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.NewPseudoId;
import com.innerfriends.friends.domain.usecase.RegisterANewFriendIntoThePlatformCommand;
import com.innerfriends.friends.domain.usecase.RegisterANewFriendIntoThePlatformUseCase;
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
public class ManagedRegisterANewFriendIntoThePlatformUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedRegisterANewFriendIntoThePlatformUseCase managedRegisterANewFriendIntoThePlatformUseCase;

    @InjectMock
    RegisterANewFriendIntoThePlatformUseCase registerANewFriendIntoThePlatformUseCase;

    @InjectMock
    InstantProvider instantProvider;

    @Test
    public void should_register_a_new_friend_into_the_platform() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        final RegisterANewFriendIntoThePlatformCommand registerANewFriendIntoThePlatformCommand
                = new RegisterANewFriendIntoThePlatformCommand(new NewPseudoId("Mario"));
        doReturn(mario).when(registerANewFriendIntoThePlatformUseCase).execute(registerANewFriendIntoThePlatformCommand);
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When && Then
        assertThat(managedRegisterANewFriendIntoThePlatformUseCase.execute(registerANewFriendIntoThePlatformCommand))
                .isEqualTo(mario);
        assertThat(((Number) entityManager.createNativeQuery(COUNT_OUTBOX_EVENT_FOR_AGGREGATE_ID_SQL)
                .setParameter(1, "Mario")
                .getSingleResult()).longValue()).isEqualTo(1l);
        assertThat(entityManager.createNativeQuery(GET_TYPE_BY_AGGREGATE_ID)
                .setParameter(1, "Mario")
                .getSingleResult())
                .isEqualTo("ANewFriendRegisteredIntoThePlatform");
    }

}
