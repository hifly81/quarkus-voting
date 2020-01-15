package com.redhat.demo.voting;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class VotingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/voting")
                .then()
                .statusCode(200)
                .body(is("hello"));
    }
}
