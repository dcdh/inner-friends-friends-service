package com.innerfriends.friends.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import java.time.Instant;

@ApplicationScoped
public class InstantProvider {

    public Instant now() {
        return Instant.now();
    }

}
