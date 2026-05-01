package ru.kvardekkvar.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static ru.kvardekkvar.config.TestConfig.APP_HOST;
import static ru.kvardekkvar.config.TestConfig.APP_PORT;

public final class RestAssuredClient {

    public static RequestSpecification defaultSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(APP_HOST)
                .setPort(APP_PORT)
                .addFilter(new AllureRestAssured())
                .build();
    }

    public <A, B> B post(String endpoint,
                         ContentType contentType,
                         ContentType acceptContentType,
                         String apiKey,
                         A requestBody,
                         Class<B> responseType) {
        return send(Method.POST, endpoint, contentType, acceptContentType, apiKey, requestBody, responseType);
    }


    public <A, B> B get(String endpoint,
                        ContentType contentType,
                        ContentType acceptContentType,
                        String apiKey,
                        Class<B> responseType) {
        return send(Method.GET, endpoint, contentType, acceptContentType, apiKey, null, responseType);
    }

    private <A, B> B send(
            Method httpMethod,
            String endpoint,
            ContentType contentType,
            ContentType acceptContentType,
            String apiKey,
            A requestBody,
            Class<B> responseType) {

        RequestSpecification request = given()
                .spec(defaultSpec())
                .accept(acceptContentType)
                .contentType(contentType);


        if (apiKey != null) {
            request.header("X-Api-Key", apiKey);
        }

        if (requestBody != null) {
            switch (contentType) {
                case JSON:
                    request.body(requestBody);
                    break;
                case URLENC:
                    if (requestBody instanceof Map<?, ?> map) {
                        request.formParams((Map<String, ?>) map);
                    } else {
                        throw new IllegalArgumentException(
                                "For form-urlencoded body must be Map"
                        );
                    }
                    break;
                default:
                    request.body(requestBody);
            }
        }

        return request.request(httpMethod, endpoint)
                .then()
                .extract()
                .as(responseType);


    }


}

