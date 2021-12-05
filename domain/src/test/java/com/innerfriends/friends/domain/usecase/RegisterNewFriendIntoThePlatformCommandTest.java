package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.NewPseudoId;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class RegisterNewFriendIntoThePlatformCommandTest {

    @Test
    public void should_verify_equality() {
        EqualsVerifier.forClass(RegisterNewFriendIntoThePlatformCommand.class).verify();
    }

    @Test
    public void should_identifier_return_owner() {
        // Given
        final NewPseudoId newPseudoId = mock(NewPseudoId.class);

        // When && Then
        assertThat(new RegisterNewFriendIntoThePlatformCommand(newPseudoId).identifier()).isEqualTo(newPseudoId);
    }

}
