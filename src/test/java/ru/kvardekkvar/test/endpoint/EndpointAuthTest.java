package ru.kvardekkvar.test.endpoint;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvardekkvar.model.endpoint.EndpointRequest;
import ru.kvardekkvar.model.endpoint.EndpointResponse;
import ru.kvardekkvar.test.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.kvardekkvar.mock.MockStubs.mockAuthFailure;

@Epic("Ручка /endpoint")
@Feature("Авторизация")
@DisplayName("/endpoint — Авторизация")
class EndpointAuthTest extends BaseTest {
    private static final String TOKEN = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Owner("aqa")
    @DisplayName("Запрос без API-ключа отклоняется")
    void shouldRejectRequestWithoutApiKeyTest() {
        EndpointRequest requestBody = new EndpointRequest(TOKEN, "LOGIN");
        EndpointResponse response = endpointApi.sendWithoutApiKey(requestBody);

        String expectedMessage = "Missing or invalid API Key";
        assertEquals("ERROR", response.getResult(), "Без API-ключа сервис должен отвечать ошибкой");
        assertEquals(expectedMessage, response.getMessage(), "Неверный текст ошибки");
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Owner("aqa")
    @DisplayName("Запрос с неправильным API-ключом отклоняется")
    void shouldRejectRequestWithWrongApiKeyTest() {
        EndpointRequest requestBody = new EndpointRequest(TOKEN, "LOGIN");
        EndpointResponse response = endpointApi.sendWithApiKey(requestBody, "12345");

        String expectedMessage = "Missing or invalid API Key";
        assertEquals("ERROR", response.getResult(), "С неправильным API-ключом сервис должен отвечать ошибкой");
        assertEquals(expectedMessage, response.getMessage(), "Неверный текст ошибки");
    }
}
