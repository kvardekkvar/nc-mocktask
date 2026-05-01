package ru.kvardekkvar.test.endpoint;

import io.qameta.allure.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvardekkvar.model.endpoint.EndpointRequest;
import ru.kvardekkvar.model.endpoint.EndpointResponse;
import ru.kvardekkvar.test.BaseTest;
import ru.kvardekkvar.test.MockedTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static ru.kvardekkvar.mock.MockStubs.mockAuthSuccess;
import static ru.kvardekkvar.mock.MockStubs.mockDoActionSuccess;
import static ru.kvardekkvar.util.TestDataUtils.generateToken;

@Epic("Ручка /endpoint")
@Feature("Удаление токена")
@DisplayName("/endpoint — LOGOUT")
class EndpointLogoutTest extends MockedTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("После LOGOUT токен теряет доступ к ACTION")
    void shouldInvalidateTokenAfterLogoutTest() {
        mockAuthSuccess();
        mockDoActionSuccess();
        String token = generateToken();

        EndpointResponse loginResponse = endpointApi.login(token);
        EndpointResponse logoutResponse = endpointApi.logout(token);
        EndpointResponse actionAfterLogout = endpointApi.action(token);

        String expectedMessage = String.format("Token '%s' not found", token);

        assertEquals("OK", loginResponse.getResult(), "LOGIN должен быть успешным");
        assertEquals("OK", logoutResponse.getResult(), "LOGOUT должен быть успешным");
        assertEquals("ERROR", actionAfterLogout.getResult(), "Токен после LOGOUT не должен выполнять ACTION");
        assertEquals(expectedMessage, actionAfterLogout.getMessage(), "Должна быть корректная причина ошибки");

    }

    @Disabled("Уточнить корректность поведения")
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Owner("aqa")
    @DisplayName("LOGOUT неуспешен для токена, не прошедшего LOGIN")
    void shouldReturnErrorWhenLoggingOutBeforeLoginTest() {
        String token = generateToken();

        EndpointResponse logoutResponse = endpointApi.logout(token);

        String expectedMessage = String.format("Token '%s' not found", token);

        assertEquals("ERROR", logoutResponse.getResult(), "LOGOUT должен быть успешным");
        assertEquals(expectedMessage, logoutResponse.getMessage(), "Должна быть корректная причина ошибки");
    }

    @Disabled("Уточнить корректность поведения")
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Owner("aqa")
    @DisplayName("Повторный LOGOUT возвращает ошибку")
    void shouldReturnErrorOnSecondLogoutTest() {
        mockAuthSuccess();
        String token = generateToken();

        EndpointResponse loginResponse = endpointApi.login(token);
        EndpointResponse logoutResponse1 = endpointApi.logout(token);
        EndpointResponse logoutResponse2 = endpointApi.logout(token);

        String expectedMessage = String.format("Token '%s' not found", token);

        assertEquals("OK", loginResponse.getResult(), "LOGIN должен быть успешным");
        assertEquals("OK", logoutResponse1.getResult(), "LOGOUT должен быть успешным (первый из двух)");
        assertEquals("ERROR", logoutResponse2.getResult(), "LOGOUT должен падать (второй из двух)");
        assertEquals(expectedMessage, logoutResponse2.getMessage(), "Должна быть корректная причина ошибки");

    }

}
