package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.Bio;
import com.innerfriends.friends.domain.ExecutedBy;
import com.innerfriends.friends.domain.FriendId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class WriteBioCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(WriteBioCommand.class).verify();
    }

    @Test
    public void should_identifier_return_friend_id() {
        // Given
        final FriendId friendId = mock(FriendId.class);

        // When && Then
        assertThat(new WriteBioCommand(friendId, mock(Bio.class), mock(ExecutedBy.class)).identifier()).isEqualTo(friendId);
    }

}
