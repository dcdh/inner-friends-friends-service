package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.AlreadyAFriendException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AlreadyAFriendExceptionMapper implements ExceptionMapper<AlreadyAFriendException> {

    @Override
    public Response toResponse(final AlreadyAFriendException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Already a friend")
                .build();
    }
}
