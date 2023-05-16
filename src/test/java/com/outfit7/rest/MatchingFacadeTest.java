package com.outfit7.rest;

import static com.outfit7.utils.TestUtils.users;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.outfit7.entity.User;
import com.outfit7.json.JsonUtils;
import com.outfit7.services.OpponentsService;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.response.Response;

@QuarkusTest
class MatchingFacadeTest {

    @InjectMock
    OpponentsService opponentsService;

    @Test
    void shouldReturnOpponentsForSelectedUserId_classic() {
        // Given
        String userId = "some-user-id";
        List<User> opponents = users();

        given(opponentsService.matchOpponentsClassic(userId))
                .willReturn(opponents);

        // When
        Response response = given()
                .when()
                .get("/matching/classic/{userId}", userId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK.getStatusCode());
        List<User> returnedOpponents = JsonUtils.deserializeToList(response.getBody().asString(), User.class);
        assertThat(returnedOpponents).isEqualTo(opponents);
    }


    @Test
    void shouldReturnOpponentsForSelectedUserId_ranked() {
        // Given
        String userId = "some-user-id";
        List<User> opponents = users();

        given(opponentsService.matchOpponentsRanked(userId))
                .willReturn(opponents);

        // When
        Response response = given()
                .when()
                .get("/matching/ranked/{userId}", userId);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK.getStatusCode());
        List<User> returnedOpponents = JsonUtils.deserializeToList(response.getBody().asString(), User.class);
        assertThat(returnedOpponents).isEqualTo(opponents);
    }
}