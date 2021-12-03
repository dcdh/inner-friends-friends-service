package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.FriendUnknownException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class FriendUnknownExceptionMapper implements ExceptionMapper<FriendUnknownException> {

    @Override
    public Response toResponse(final FriendUnknownException exception) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
