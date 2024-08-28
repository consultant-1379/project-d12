package com.ericsson.graduates.microservicemanager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MicroserviceTests {
    @Test
    void contextLoads() {
    }

    @Test
    void addDependency_AddSuccessfully() {
        Microservice msC = new Microservice("serv3", null, "the service", new Date(), Collections.singletonList("0.0.3"));
        Microservice msD = new Microservice("serv4", null, "the service", new Date(), Collections.singletonList("0.0.4"), "test1");
        msD.addDependency(msC);
        assertThat(msD, hasProperty("dependencies", contains(msC)));
        assertThat(msC, hasProperty("parent", is(msD)));
    }

    @Test
    void addDependency_AddFailsOnNullChild() {
        Microservice msC = new Microservice("serv3", null, "the service", new Date(), Collections.singletonList("0.0.3"));
        Microservice msD = new Microservice("serv4", null, "the service", new Date(), Collections.singletonList("0.0.4"), "test1");
        msD.addDependency(msC);
        assertThrows(IllegalArgumentException.class, () -> {
            msD.addDependency(null);
        });
        // Make sure the rest of the properties remain intact after trying to add null
        assertThat(msD, hasProperty("dependencies", contains(msC)));
        assertThat(msC, hasProperty("parent", is(msD)));
    }

    @Test
    void addVersion_AddSuccessfully() {
        Microservice msC = new Microservice("serv3", null, "the service", new Date(), new ArrayList<>());
        msC.addVersion("0.0.5");
        assertThat(msC, hasProperty("versionNumbers", contains("0.0.5")));
    }

    @Test
    void addVersion_RejectsDuplicate() {
        Microservice msC = new Microservice("serv3", null, "the service", new Date(), new ArrayList<>());
        msC.addVersion("0.0.5");
        msC.addVersion("0.0.5");
        assertThat(msC, hasProperty("versionNumbers", hasSize(1)));
    }

    @Test
    void addVersion_FailsOnNullValue() {
        Microservice msC = new Microservice("serv3", null, "the service", new Date(), new ArrayList<>());
        msC.addVersion("0.0.5");
        assertThrows(IllegalArgumentException.class, () -> {
            msC.addVersion(null);
        });
        // Ensure existing versions remain unaffected
        assertThat(msC, hasProperty("versionNumbers", contains("0.0.5")));
    }
}
