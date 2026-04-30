package ru.kvardekkvar.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;

import static ru.kvardekkvar.config.TestConfig.APP_HOST;
import static ru.kvardekkvar.config.TestConfig.APP_PORT;

public final class RestAssuredClientConfig {

    public static void configure() {
        RestAssured.baseURI = APP_HOST;
        RestAssured.port = APP_PORT;
        RestAssured.filters(new AllureRestAssured());
    }
}
