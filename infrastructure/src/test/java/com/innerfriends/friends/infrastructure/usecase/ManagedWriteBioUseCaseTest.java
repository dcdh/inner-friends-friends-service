package com.innerfriends.friends.infrastructure.usecase;

import com.innerfriends.friends.domain.Bio;
import com.innerfriends.friends.domain.ExecutedBy;
import com.innerfriends.friends.domain.Friend;
import com.innerfriends.friends.domain.FriendId;
import com.innerfriends.friends.domain.usecase.WriteBioCommand;
import com.innerfriends.friends.domain.usecase.WriteBioUseCase;
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
public class ManagedWriteBioUseCaseTest extends ManagedUseCaseTest {

    @Inject
    ManagedWriteBioUseCase managedWriteBioUseCase;

    @InjectMock
    WriteBioUseCase writeBioUseCase;

    @InjectMock
    InstantProvider instantProvider;

    @Test
    public void should_() {
        // Given
        final Friend mario = new Friend(new FriendId("Mario"));
        mario.writeBio(new Bio("super plumber"), new ExecutedBy("Mario"));
        final WriteBioCommand writeBioCommand = new WriteBioCommand(new FriendId("Mario"), new Bio("super plumber"), new ExecutedBy("Mario"));
        doReturn(mario).when(writeBioUseCase).execute(writeBioCommand);
        doReturn(Instant.ofEpochSecond(1)).when(instantProvider).now();

        // When && Then
        assertThat(managedWriteBioUseCase.execute(writeBioCommand)).isEqualTo(mario);
        assertThat(((Number) entityManager.createNativeQuery(COUNT_OUTBOX_EVENT_FOR_AGGREGATE_ID_SQL)
                .setParameter(1, "Mario")
                .getSingleResult()).longValue()).isEqualTo(1l);
        assertThat(entityManager.createNativeQuery(GET_TYPE_BY_AGGREGATE_ID)
                .setParameter(1, "Mario")
                .getSingleResult())
                .isEqualTo("BioWritten");
    }

}
