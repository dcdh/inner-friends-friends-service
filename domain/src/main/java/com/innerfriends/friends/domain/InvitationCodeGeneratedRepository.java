package com.innerfriends.friends.domain;

public interface InvitationCodeGeneratedRepository {

    void store(InvitationCodeGenerated invitationCodeGenerated);

    InvitationCodeGenerated get(InvitationCode invitationCode) throws UnknownInvitationCodeException;

}
