package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.UnknownInvitationCodeException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class UnknownInvitationCodeExceptionMapper implements ExceptionMapper<UnknownInvitationCodeException> {

    @Override
    public Response toResponse(final UnknownInvitationCodeException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Unknown invitation code")
                .build();
    }
}
