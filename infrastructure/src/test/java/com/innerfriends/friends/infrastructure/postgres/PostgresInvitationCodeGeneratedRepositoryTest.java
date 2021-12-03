package com.innerfriends.friends.infrastructure.postgres;

import com.innerfriends.friends.domain.FromFriendId;
import com.innerfriends.friends.domain.GeneratedAt;
import com.innerfriends.friends.domain.InvitationCode;
import com.innerfriends.friends.domain.InvitationCodeGenerated;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.RollbackException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@ExtendWith(MockitoExtension.class)
public class PostgresInvitationCodeGeneratedRepositoryTest extends RepositoryTesting {

    public static final String COUNT_INVITATION_CODE_GENERATED_SQL = "SELECT COUNT(*) FROM public.T_INVITATION_CODE_GENERATED";

    @Inject
    EntityManager entityManager;

    @Inject
    PostgresInvitationCodeGeneratedRepository postgresInvitationCodeGeneratedRepository;

    @Test
    public void should_store_invitation_code() throws Exception {
        // Given
        final LocalDateTime generatedAt = LocalDateTime.now(ZoneOffset.UTC);

        // When
        runInTransaction(() -> {
            postgresInvitationCodeGeneratedRepository.store(new InvitationCodeGenerated(new FromFriendId("Mario"),
                    new InvitationCode(new UUID(0, 0)), new GeneratedAt(generatedAt)));
            return null;
        });

        // Then
        assertThat(((Number) entityManager.createNativeQuery(COUNT_INVITATION_CODE_GENERATED_SQL)
                .getSingleResult()).longValue()).isEqualTo(1l);
        assertThat(entityManager.find(InvitationCodeGeneratedEntity.class, new UUID(0, 0)))
                .isEqualTo(new InvitationCodeGeneratedEntity(new UUID(0, 0), "Mario", generatedAt));
    }

    @Test
    public void should_store_fail_when_invitation_code_already_used() throws Exception {
        // Given
        runInTransaction(() -> {
            postgresInvitationCodeGeneratedRepository.store(new InvitationCodeGenerated(new FromFriendId("Mario"),
                    new InvitationCode(new UUID(0, 0)), new GeneratedAt(LocalDateTime.now(ZoneOffset.UTC))));
            return null;
        });

        // When && Then
        assertThatThrownBy(() ->
                runInTransaction(() -> {
                    postgresInvitationCodeGeneratedRepository.store(
                            new InvitationCodeGenerated(new FromFriendId("Mario"),
                                    new InvitationCode(new UUID(0, 0)), new GeneratedAt(LocalDateTime.now())));
                    return null;
                }))
                .isInstanceOf(RollbackException.class);
    }

    @Test
    public void should_get_invitation_code() throws Exception {
        // Given
        final LocalDateTime generatedAt = LocalDateTime.now(ZoneOffset.UTC);
        runInTransaction(() -> {
            postgresInvitationCodeGeneratedRepository.store(new InvitationCodeGenerated(new FromFriendId("Mario"),
                    new InvitationCode(new UUID(0, 0)), new GeneratedAt(generatedAt)));
            return null;
        });

        // When
        final InvitationCodeGenerated invitationCodeGenerated = runInTransaction(() -> postgresInvitationCodeGeneratedRepository.get(new InvitationCode(new UUID(0, 0))));

        // Then
        assertThat(invitationCodeGenerated).isEqualTo(new InvitationCodeGenerated(new FromFriendId("Mario"),
                new InvitationCode(new UUID(0, 0)), new GeneratedAt(generatedAt)));
    }
}
