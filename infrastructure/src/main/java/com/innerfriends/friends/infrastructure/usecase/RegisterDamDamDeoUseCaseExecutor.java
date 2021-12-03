package com.innerfriends.friends.infrastructure.usecase;

import io.quarkus.runtime.StartupEvent;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Objects;

@ApplicationScoped
public class RegisterDamDamDeoUseCaseExecutor {

    private final RegisterDamDamDeoUseCase registerDamDamDeoUseCase;

    public RegisterDamDamDeoUseCaseExecutor(final RegisterDamDamDeoUseCase registerDamDamDeoUseCase) {
        this.registerDamDamDeoUseCase = Objects.requireNonNull(registerDamDamDeoUseCase);
    }

    public void onStart(@Observes @Priority(2) final StartupEvent ev) {
        registerDamDamDeoUseCase.execute(new RegisterDamDamDeoCommand());
    }

}
