package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.NotInFriendshipWithException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotInFriendshipWithExceptionMapper implements ExceptionMapper<NotInFriendshipWithException> {

    @Override
    public Response toResponse(final NotInFriendshipWithException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("Not in friendship with")
                .build();
    }

}
