package it.acme.tvseries.adapter.in.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
class TvSeriesResourceIT {

    @Test
    void shouldCreateTvSeries() {
        String requestBody = """
            {
                "titolo": "Breaking Bad IT",
                "anno": 2008,
                "genere": "Crime Drama",
                "regista": "Vince Gilligan",
                "sinossi": "A high school chemistry teacher turned methamphetamine producer."
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201)
            .header("Location", notNullValue())
            .body("id", notNullValue())
            .body("titolo", equalTo("Breaking Bad IT"))
            .body("anno", equalTo(2008))
            .body("genere", equalTo("Crime Drama"))
            .body("regista", equalTo("Vince Gilligan"));
    }

    @Test
    void shouldRejectInvalidTvSeriesOnCreate() {
        String requestBody = """
            {
                "titolo": "",
                "anno": 1800,
                "genere": "Drama",
                "regista": "Test",
                "sinossi": "Test"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(400)
            .body("type", equalTo("about:blank"))
            .body("title", equalTo("Constraint Violation"))
            .body("status", equalTo(400))
            .body("violations", notNullValue());
    }

    @Test
    void shouldRejectDuplicateTvSeriesOnCreate() {
        String requestBody = """
            {
                "titolo": "The Wire",
                "anno": 2002,
                "genere": "Crime Drama",
                "regista": "David Simon",
                "sinossi": "Test"
            }
            """;

        // First insert
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201);

        // Second insert (duplicate)
        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(409)
            .body("type", equalTo("about:blank"))
            .body("title", equalTo("Duplicate Entry"))
            .body("status", equalTo(409));
    }

    @Test
    void shouldGetTvSeriesById() {
        // Create a series
        String createBody = """
            {
                "titolo": "Stranger Things IT",
                "anno": 2016,
                "genere": "Sci-Fi Horror",
                "regista": "The Duffer Brothers",
                "sinossi": "A group of kids encounter supernatural forces."
            }
            """;

        String id = given()
            .contentType(ContentType.JSON)
            .body(createBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201)
            .extract().path("id");

        // Get by ID
        given()
        .when()
            .get("/api/series/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("titolo", equalTo("Stranger Things IT"))
            .body("anno", equalTo(2016));
    }

    @Test
    void shouldReturn404WhenTvSeriesNotFound() {
        String nonExistentId = "00000000-0000-0000-0000-000000000000";

        given()
        .when()
            .get("/api/series/" + nonExistentId)
        .then()
            .statusCode(404)
            .body("type", equalTo("about:blank"))
            .body("title", equalTo("Not Found"))
            .body("status", equalTo(404));
    }

    @Test
    void shouldReturn400WhenInvalidUUID() {
        given()
        .when()
            .get("/api/series/invalid-uuid")
        .then()
            .statusCode(400)
            .body("type", equalTo("about:blank"))
            .body("title", equalTo("Bad Request"))
            .body("status", equalTo(400));
    }

    @Test
    void shouldUpdateTvSeries() {
        // Create a series
        String createBody = """
            {
                "titolo": "Dark IT",
                "anno": 2017,
                "genere": "Thriller",
                "regista": "Baran bo Odar",
                "sinossi": "Original synopsis"
            }
            """;

        String id = given()
            .contentType(ContentType.JSON)
            .body(createBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201)
            .extract().path("id");

        // Update
        String updateBody = """
            {
                "titolo": "Dark IT Updated",
                "anno": 2017,
                "genere": "Sci-Fi Thriller",
                "regista": "Baran bo Odar",
                "sinossi": "Updated synopsis"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updateBody)
        .when()
            .put("/api/series/" + id)
        .then()
            .statusCode(200)
            .body("id", equalTo(id))
            .body("titolo", equalTo("Dark IT Updated"))
            .body("genere", equalTo("Sci-Fi Thriller"))
            .body("sinossi", equalTo("Updated synopsis"));
    }

    @Test
    void shouldReturn404WhenUpdatingNonExistentTvSeries() {
        String nonExistentId = "00000000-0000-0000-0000-000000000001";
        String updateBody = """
            {
                "titolo": "Test",
                "anno": 2020,
                "genere": "Drama",
                "regista": "Test",
                "sinossi": "Test"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updateBody)
        .when()
            .put("/api/series/" + nonExistentId)
        .then()
            .statusCode(404);
    }

    @Test
    void shouldRejectInvalidTvSeriesOnUpdate() {
        // Create a series
        String createBody = """
            {
                "titolo": "Valid Title IT",
                "anno": 2020,
                "genere": "Drama",
                "regista": "Director",
                "sinossi": "Synopsis"
            }
            """;

        String id = given()
            .contentType(ContentType.JSON)
            .body(createBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201)
            .extract().path("id");

        // Try to update with invalid data
        String updateBody = """
            {
                "titolo": "",
                "anno": 1800,
                "genere": "Drama",
                "regista": "Director",
                "sinossi": "Synopsis"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(updateBody)
        .when()
            .put("/api/series/" + id)
        .then()
            .statusCode(400)
            .body("type", equalTo("about:blank"))
            .body("title", equalTo("Constraint Violation"))
            .body("violations", notNullValue());
    }

    @Test
    void shouldDeleteTvSeries() {
        // Create a series
        String createBody = """
            {
                "titolo": "To Delete IT",
                "anno": 2021,
                "genere": "Action",
                "regista": "Test Director",
                "sinossi": "Will be deleted"
            }
            """;

        String id = given()
            .contentType(ContentType.JSON)
            .body(createBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201)
            .extract().path("id");

        // Delete
        given()
        .when()
            .delete("/api/series/" + id)
        .then()
            .statusCode(204);

        // Verify it's deleted
        given()
        .when()
            .get("/api/series/" + id)
        .then()
            .statusCode(404);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistentTvSeries() {
        String nonExistentId = "00000000-0000-0000-0000-000000000002";

        given()
        .when()
            .delete("/api/series/" + nonExistentId)
        .then()
            .statusCode(404);
    }

    @Test
    void shouldListTvSeriesWithPagination() {
        // Create multiple series
        for (int i = 1; i <= 3; i++) {
            String body = String.format("""
                {
                    "titolo": "Series %d IT",
                    "anno": %d,
                    "genere": "Drama",
                    "regista": "Director %d",
                    "sinossi": "Synopsis %d"
                }
                """, i, 2020 + i, i, i);

            given()
                .contentType(ContentType.JSON)
                .body(body)
            .when()
                .post("/api/series")
            .then()
                .statusCode(201);
        }

        // List all
        given()
            .queryParam("page", 0)
            .queryParam("size", 20)
        .when()
            .get("/api/series")
        .then()
            .statusCode(200)
            .body("items.size()", greaterThan(0))
            .body("page", equalTo(0))
            .body("size", equalTo(20))
            .body("total", greaterThan(0));
    }

    @Test
    void shouldFilterTvSeriesByGenere() {
        // Create series with different genres
        String createBody1 = """
            {
                "titolo": "Sci-Fi Test IT",
                "anno": 2020,
                "genere": "Sci-Fi",
                "regista": "Director",
                "sinossi": "Test"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(createBody1)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201);

        // Filter by genere
        given()
            .queryParam("genere", "Sci-Fi")
        .when()
            .get("/api/series")
        .then()
            .statusCode(200)
            .body("items.size()", greaterThan(0));
    }

    @Test
    void shouldSearchTvSeriesByQuery() {
        // Create a series
        String createBody = """
            {
                "titolo": "Searchable Series IT",
                "anno": 2022,
                "genere": "Drama",
                "regista": "Search Director",
                "sinossi": "This is searchable"
            }
            """;

        given()
            .contentType(ContentType.JSON)
            .body(createBody)
        .when()
            .post("/api/series")
        .then()
            .statusCode(201);

        // Search
        given()
            .queryParam("q", "Searchable")
        .when()
            .get("/api/series")
        .then()
            .statusCode(200)
            .body("items.size()", greaterThan(0));
    }
}
