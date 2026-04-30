package ru.kvardekkvar.mock;

import io.qameta.allure.Step;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class MockStubs {


    @Step("Мокаем внешний сервис /auth: успешный ответ (HTTP 200)")
    public static void mockAuthSuccess() {
        stubAuth(200, "mock/auth/200_success.json");
    }

    @Step("Мокаем внешний сервис /auth: ошибка авторизации (HTTP 401)")
    public static void mockAuthFailure() {
        stubAuth(401, "mock/auth/401_unauthorized.json");
    }

    @Step("Мокаем внешний сервис /doAction: успешное действие (HTTP 200)")
    public static void mockDoActionSuccess() {
        stubDoAction(200, "mock/doAction/200_success.json");
    }

    @Step("Мокаем внешний сервис /doAction: ошибка сервиса (HTTP 500)")
    public static void mockDoActionFailure() {
        stubDoAction(500, "mock/doAction/500_internal_error.json");
    }


    private static void stubAuth(int status, String jsonPath) {
        stubFor(post(urlPathEqualTo("/auth"))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(loadJson(jsonPath))));
    }

    private static void stubDoAction(int status, String jsonPath) {
        stubFor(post(urlPathEqualTo("/doAction"))
                .willReturn(aResponse()
                        .withStatus(status)
                        .withHeader("Content-Type", "application/json")
                        .withBody(loadJson(jsonPath))));
    }

    private static String loadJson(String resourcePath) {
        try (InputStream is = MockStubs.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Mock JSON not found: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read mock resource: " + resourcePath, e);
        }
    }

}
