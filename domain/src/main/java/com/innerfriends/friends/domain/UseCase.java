package com.innerfriends.friends.domain;

public interface UseCase<R, C extends UseCaseCommand> {

    R execute(C command);

}
