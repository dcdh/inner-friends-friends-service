package com.innerfriends.friends.infrastructure.resources;

import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(HazelcastTestResourceLifecycleManager.class)
@QuarkusTestResource(OpenTelemetryLifecycleManager.class)
@QuarkusTestResource(RedpandaTestResourceLifecycleManager.class)
@QuarkusTestResource(ArangoDBTestResourceLifecycleManager.class)
public class QuarkusTestResources {

}
