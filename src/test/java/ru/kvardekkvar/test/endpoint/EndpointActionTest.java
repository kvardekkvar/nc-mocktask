package ru.kvardekkvar.test.endpoint;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvardekkvar.model.endpoint.EndpointResponse;
import ru.kvardekkvar.test.MockedTest;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.kvardekkvar.mock.MockStubs.mockAuthSuccess;
import static ru.kvardekkvar.mock.MockStubs.mockDoActionFailure;
import static ru.kvardekkvar.mock.MockStubs.mockDoActionSuccess;
import static ru.kvardekkvar.util.TestDataUtils.generateToken;

@Epic("Ручка /endpoint")
@Feature("Использование токена")
@DisplayName("/endpoint — ACTION")
class EndpointActionTest extends MockedTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Owner("aqa")
    @DisplayName("ACTION с токеном, прошедшим LOGIN, вызывает /doAction и возвращается успешно")
    void shouldSuccessfullyPerformActionAfterLoginTest() {
        mockAuthSuccess();
        mockDoActionSuccess();

        String token = generateToken();

        EndpointResponse response1 = endpointApi.login(token);
        EndpointResponse response2 = endpointApi.action(token);

        assertEquals("OK", response1.getResult(), "LOGIN должен быть успешен");
        assertEquals("OK", response2.getResult(), "ACTION с токеном, прошедшим LOGIN, должен быть успешен");
        wireMock().verify(exactly(1), postRequestedFor(urlEqualTo("/doAction")));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("Если /doAction возвращает ошибку, ACTION должен завершиться с ошибкой")
    void shouldReturnErrorWhenActionEndpointFails() {
        mockAuthSuccess();
        mockDoActionFailure();

        String token = generateToken();
        EndpointResponse login = endpointApi.login(token);
        EndpointResponse action = endpointApi.action(token);

        assertEquals("OK", login.getResult(), "LOGIN должен быть успешен");
        assertEquals("ERROR", action.getResult(), "ACTION должен падать при ошибке внешнего сервиса");
        assertNotNull(action.getMessage(), "Должна быть причина ошибки");
        //TODO: уточнить текст ошибки
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Owner("aqa")
    @DisplayName("ACTION с токеном, не прошедшим LOGIN, возвращает ошибку и не обращается к /doAction")
    void shouldRejectActionWithoutLoginTest() {
        mockDoActionSuccess();

        String token = generateToken();

        EndpointResponse response = endpointApi.action(token);

        String expectedMessage = String.format("Token '%s' not found", token);

        assertEquals("ERROR", response.getResult(), "ACTION без авторизации должен быть отклонен");
        assertEquals(expectedMessage, response.getMessage(), "Должна быть корректная причина ошибки");
        wireMock().verify(exactly(0), postRequestedFor(urlEqualTo("/doAction")));
    }
}
