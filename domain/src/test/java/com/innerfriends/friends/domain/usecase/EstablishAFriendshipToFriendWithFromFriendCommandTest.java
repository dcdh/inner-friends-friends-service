package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.ExecutedBy;
import com.innerfriends.friends.domain.InvitationCode;
import com.innerfriends.friends.domain.ToFriendId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class EstablishAFriendshipToFriendWithFromFriendCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(EstablishAFriendshipToFriendWithFromFriendCommand.class).verify();
    }

    @Test
    public void should_identifier_return_to_friend_id() {
        // Given
        final ToFriendId toFriendId = mock(ToFriendId.class);

        // When && Then
        assertThat(new EstablishAFriendshipToFriendWithFromFriendCommand(toFriendId, mock(InvitationCode.class), mock(ExecutedBy.class))
                .identifier()).isEqualTo(toFriendId);
    }

}
