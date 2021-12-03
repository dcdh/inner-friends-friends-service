package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.FromFriendId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GenerateInvitationCodeCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(GenerateInvitationCodeCommand.class).verify();
    }

    @Test
    public void should_identifier_return_from_friend_id() {
        // Given
        final FromFriendId fromFriendId = mock(FromFriendId.class);

        // When && Then
        assertThat(new GenerateInvitationCodeCommand(fromFriendId).identifier()).isEqualTo(fromFriendId);
    }

}
