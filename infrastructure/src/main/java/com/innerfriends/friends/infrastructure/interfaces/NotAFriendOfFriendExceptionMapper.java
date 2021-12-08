package com.innerfriends.friends.infrastructure.interfaces;

import com.innerfriends.friends.domain.NotAFriendOfFriendException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAFriendOfFriendExceptionMapper implements ExceptionMapper<NotAFriendOfFriendException> {

    @Override
    public Response toResponse(final NotAFriendOfFriendException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("Not a friend of friend")
                .build();
    }

}
