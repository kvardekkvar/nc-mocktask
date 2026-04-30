package ru.kvardekkvar.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.kvardekkvar.model.endpoint.EndpointRequest;
import ru.kvardekkvar.model.endpoint.EndpointResponse;

import static ru.kvardekkvar.config.TestConfig.API_KEY;

public class EndpointApi {

    public EndpointResponse send(EndpointRequest request) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .header("X-Api-Key", API_KEY)
                .formParam("token", request.getToken())
                .formParam("action", request.getAction())
                .when()
                .post("/endpoint")
                .then()
                .extract()
                .response();

        return response.as(EndpointResponse.class);
    }

    public EndpointResponse sendWithoutApiKey(EndpointRequest request) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .formParam("token", request.getToken())
                .formParam("action", request.getAction())
                .when()
                .post("/endpoint")
                .then()
                .extract()
                .response();

        return response.as(EndpointResponse.class);
    }

    public EndpointResponse sendWithApiKey(EndpointRequest request, String apiKey) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .header("X-Api-Key", apiKey)
                .formParam("token", request.getToken())
                .formParam("action", request.getAction())
                .when()
                .post("/endpoint")
                .then()
                .extract()
                .response();

        return response.as(EndpointResponse.class);
    }
}
