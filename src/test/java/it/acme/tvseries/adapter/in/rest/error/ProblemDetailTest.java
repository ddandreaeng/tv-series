package it.acme.tvseries.adapter.in.rest.error;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import it.acme.tvseries.adapter.in.rest.dto.CreateTvSeriesRequest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProblemDetailTest {

    @Test
    void shouldReturn400WithProblemJsonForValidationError() {
        CreateTvSeriesRequest invalidRequest = new CreateTvSeriesRequest(
            "",  // titolo blank - violation
            1800,  // anno < 1900 - violation
            "",  // genere blank - violation
            "",  // regista blank - violation
            null
        );

        given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
        .when()
            .post("/api/series")
        .then()
            .statusCode(400)
            .contentType("application/problem+json")
            .body("type", equalTo("https://example.com/problems/validation-error"))
            .body("title", equalTo("Validation error"))
            .body("status", equalTo(400))
            .body("detail", equalTo("Request validation failed"))
            .body("errorCode", equalTo("VALIDATION_ERROR"))
            .body("errors", notNullValue())
            .body("errors.titolo", notNullValue())
            .body("errors.anno", notNullValue())
            .body("errors.genere", notNullValue())
            .body("errors.regista", notNullValue());
    }

    @Test
    void shouldReturn404WithProblemJsonWhenResourceNotFound() {
        given()
        .when()
            .get("/api/series/00000000-0000-0000-0000-000000000001")
        .then()
            .statusCode(404)
            .contentType("application/problem+json")
            .body("type", equalTo("https://example.com/problems/not-found"))
            .body("title", equalTo("Resource not found"))
            .body("status", equalTo(404))
            .body("errorCode", equalTo("NOT_FOUND"))
            .body("detail", containsString("not found"));
    }

    @Test
    void shouldReturn400WithProblemJsonForInvalidUUID() {
        given()
        .when()
            .get("/api/series/not-a-valid-uuid")
        .then()
            .statusCode(400)
            .contentType("application/problem+json")
            .body("type", equalTo("https://example.com/problems/bad-request"))
            .body("title", equalTo("Bad request"))
            .body("status", equalTo(400))
            .body("errorCode", equalTo("BAD_REQUEST"));
    }

    @Test
    void shouldReturn409WithProblemJsonForDuplicate() {
        // First create a series
        CreateTvSeriesRequest request = new CreateTvSeriesRequest(
            "Breaking Bad",
            2008,
            "Crime",
            "Vince Gilligan",
            "A chemistry teacher turns to cooking meth"
        );

        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201);

        // Try to create the same series again (should conflict)
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/api/series")
        .then()
            .statusCode(409)
            .contentType("application/problem+json")
            .body("type", equalTo("https://example.com/problems/conflict"))
            .body("title", equalTo("Resource conflict"))
            .body("status", equalTo(409))
            .body("errorCode", equalTo("CONFLICT"));
    }
}
