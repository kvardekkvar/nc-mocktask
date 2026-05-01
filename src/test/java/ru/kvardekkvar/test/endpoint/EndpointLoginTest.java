package ru.kvardekkvar.test.endpoint;

import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvardekkvar.model.endpoint.EndpointRequest;
import ru.kvardekkvar.model.endpoint.EndpointResponse;
import ru.kvardekkvar.test.MockedTest;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.kvardekkvar.mock.MockStubs.mockAuthFailure;
import static ru.kvardekkvar.mock.MockStubs.mockAuthSuccess;
import static ru.kvardekkvar.util.TestDataUtils.generateToken;

@Epic("Ручка /endpoint")
@Feature("Регистрация токена")
@DisplayName("/endpoint — LOGIN")
class EndpointLoginTest extends MockedTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("LOGIN с валидным токеном возвращает OK и вызывает /auth")
    void shouldLoginSuccessfullyTest() {
        mockAuthSuccess();

        String token = generateToken();

        EndpointResponse response = endpointApi.login(token);

        assertEquals("OK", response.getResult(), "LOGIN должен завершаться успешно");
        wireMock().verify(exactly(1), postRequestedFor(urlEqualTo("/auth")));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Owner("aqa")
    @DisplayName("LOGIN возвращает ошибку, если /auth вернул ошибку")
    void shouldReturnErrorWhenAuthNotSuccessfulTest() {
        mockAuthFailure();

        String token = generateToken();

        EndpointResponse response = endpointApi.login(token);

        assertEquals("ERROR", response.getResult(), "LOGIN должен завершиться с ошибкой, так как /auth вернул ошибку");
        assertNotNull(response.getMessage(), "В ошибке должна быть причина");
        //TODO: уточнить текст ошибки
    }

    @Disabled("Нужно уточнить корректность поведения")
    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("Повторный LOGIN с одинаковым токеном")
    void shouldLoginSuccessfullyTwiceTest() {
        mockAuthSuccess();

        String token = generateToken();

        EndpointResponse response1 = endpointApi.login(token);
        EndpointResponse response2 = endpointApi.login(token);

        assertEquals("OK", response1.getResult(), "LOGIN должен завершаться успешно (первый запрос из двух)");
        assertEquals("OK", response2.getResult(), "LOGIN должен завершаться успешно (второй запрос из двух)");
        wireMock().verify(exactly(2), postRequestedFor(urlEqualTo("/auth")));
    }
}
