package com.innerfriends.friends.infrastructure.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.innerfriends.friends.infrastructure.InstantProvider;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public abstract class EventTest {

    protected InstantProvider instantProvider;

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.instantProvider = mock(InstantProvider.class);
    }

}
