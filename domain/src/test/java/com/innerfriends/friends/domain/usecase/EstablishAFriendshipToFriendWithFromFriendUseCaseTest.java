package com.innerfriends.friends.domain.usecase;

import com.innerfriends.friends.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstablishAFriendshipToFriendWithFromFriendUseCaseTest {

    private EstablishAFriendshipToFriendWithFromFriendUseCase establishAFriendshipToFriendWithFromFriendUseCase;
    private InvitationCodeGeneratedRepository invitationCodeGeneratedRepository;
    private FriendRepository friendRepository;
    private LocalDateTimeProvider localDateTimeProvider;

    @BeforeEach
    public void setup() {
        invitationCodeGeneratedRepository = mock(InvitationCodeGeneratedRepository.class);
        friendRepository = mock(FriendRepository.class);
        localDateTimeProvider = mock(LocalDateTimeProvider.class);
        establishAFriendshipToFriendWithFromFriendUseCase = new EstablishAFriendshipToFriendWithFromFriendUseCase(invitationCodeGeneratedRepository, friendRepository, localDateTimeProvider);
    }

    @Test
    public void should_establish_a_friendship_with_from_friend() {
        // Given
        final InvitationCodeGenerated invitationCodeGenerated = mock(InvitationCodeGenerated.class);
        final InvitationCode invitationCode = mock(InvitationCode.class);
        doReturn(invitationCodeGenerated).when(invitationCodeGeneratedRepository).get(invitationCode);
        doReturn(true).when(invitationCodeGenerated).isValid(localDateTimeProvider);
        doReturn(new FromFriendId("Mario")).when(invitationCodeGenerated).fromFriendId();

        final EstablishAFriendshipToFriendWithFromFriendCommand establishAFriendshipToFriendWithFromFriendCommand
                = new EstablishAFriendshipToFriendWithFromFriendCommand(new ToFriendId("Peach"), invitationCode, new ExecutedBy("Peach"));
        doReturn(new Friend(new FriendId("Peach"))).when(friendRepository).getBy(new FriendId("Peach"));

        // When
        final Friend friend = establishAFriendshipToFriendWithFromFriendUseCase.execute(establishAFriendshipToFriendWithFromFriendCommand);

        // Then
        final Friend expectedPeach = new Friend(new FriendId("Peach"), List.of(new InFriendshipWithId("DamDamDeo"), new InFriendshipWithId("Mario")), new Bio(), new Version(1l));
        assertThat(friend).isEqualTo(expectedPeach);
        verify(friendRepository).save(expectedPeach);
    }

    @Test
    public void should_fail_when_invitation_code_is_invalid() {
        // Given
        final InvitationCodeGenerated invitationCodeGenerated = mock(InvitationCodeGenerated.class);
        final InvitationCode invitationCode = mock(InvitationCode.class);
        doReturn(invitationCodeGenerated).when(invitationCodeGeneratedRepository).get(invitationCode);
        doReturn(false).when(invitationCodeGenerated).isValid(localDateTimeProvider);

        final EstablishAFriendshipToFriendWithFromFriendCommand establishAFriendshipToFriendWithFromFriendCommand
                = new EstablishAFriendshipToFriendWithFromFriendCommand(new ToFriendId("Peach"), invitationCode, new ExecutedBy("Mario"));

        // When && Then
        assertThatThrownBy(() -> establishAFriendshipToFriendWithFromFriendUseCase.execute(establishAFriendshipToFriendWithFromFriendCommand))
                .isInstanceOf(InvitationCodeInvalideException.class);
    }

    @Test
    public void should_fail_fast_when_establishing_a_friendship_with_yourself() {
        // Given
        final InvitationCodeGenerated invitationCodeGenerated = mock(InvitationCodeGenerated.class);
        final InvitationCode invitationCode = mock(InvitationCode.class);
        doReturn(invitationCodeGenerated).when(invitationCodeGeneratedRepository).get(invitationCode);
        doReturn(true).when(invitationCodeGenerated).isValid(localDateTimeProvider);
        doReturn(new FromFriendId("Peach")).when(invitationCodeGenerated).fromFriendId();

        final EstablishAFriendshipToFriendWithFromFriendCommand establishAFriendshipToFriendWithFromFriendCommand
                = new EstablishAFriendshipToFriendWithFromFriendCommand(new ToFriendId("Peach"), invitationCode, new ExecutedBy("Peach"));
        doReturn(new Friend(new FriendId("Peach"))).when(friendRepository).getBy(new FriendId("Peach"));

        // When && Then
        assertThatThrownBy(() -> establishAFriendshipToFriendWithFromFriendUseCase.execute(establishAFriendshipToFriendWithFromFriendCommand))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("You could not add yourself as a friend");
    }

    @Test
    public void should_fail_fast_when_friend_already_a_friend() {
        // Given
        final InvitationCodeGenerated invitationCodeGenerated = mock(InvitationCodeGenerated.class);
        final InvitationCode invitationCode = mock(InvitationCode.class);
        doReturn(invitationCodeGenerated).when(invitationCodeGeneratedRepository).get(invitationCode);
        doReturn(true).when(invitationCodeGenerated).isValid(localDateTimeProvider);
        doReturn(new FromFriendId("Mario")).when(invitationCodeGenerated).fromFriendId();

        final EstablishAFriendshipToFriendWithFromFriendCommand establishAFriendshipToFriendWithFromFriendCommand
                = new EstablishAFriendshipToFriendWithFromFriendCommand(new ToFriendId("Peach"), invitationCode, new ExecutedBy("Peach"));
        doReturn(new Friend(new FriendId("Peach"), List.of(new InFriendshipWithId("Mario")), new Bio(), new Version(1l))).when(friendRepository).getBy(new FriendId("Peach"));

        // When && Then
        assertThatThrownBy(() -> establishAFriendshipToFriendWithFromFriendUseCase.execute(establishAFriendshipToFriendWithFromFriendCommand))
                .isInstanceOf(AlreadyAFriendException.class);
    }

}
