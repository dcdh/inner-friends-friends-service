package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.InvitationCodeInvalideException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvitationCodeInvalideExceptionMapper implements ExceptionMapper<InvitationCodeInvalideException> {

    @Override
    public Response toResponse(final InvitationCodeInvalideException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Invitation code invalid. Please generate a new one.")
                .build();
    }
}
