package com.innerfriends.friends.domain;

public class FailedToMutateAggregateException extends RuntimeException {

    public FailedToMutateAggregateException(final Throwable cause) {
        super(cause);
    }
}
