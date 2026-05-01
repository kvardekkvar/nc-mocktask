package ru.kvardekkvar.test.endpoint;

import io.qameta.allure.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kvardekkvar.model.endpoint.EndpointRequest;
import ru.kvardekkvar.model.endpoint.EndpointResponse;
import ru.kvardekkvar.test.BaseTest;
import ru.kvardekkvar.test.MockedTest;

import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.kvardekkvar.mock.MockStubs.mockAuthSuccess;
import static ru.kvardekkvar.mock.MockStubs.mockDoActionSuccess;
import static ru.kvardekkvar.util.TestDataUtils.generateToken;

@Epic("Ручка /endpoint")
@Feature("Сквозные сценарии")
@DisplayName("/endpoint — Сквозные сценарии")
class EndpointE2ETest extends MockedTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("Полный пользовательский путь: LOGIN -> ACTION -> LOGOUT")
    void shouldExecuteEndToEndUserFlowTest() {
        mockAuthSuccess();
        mockDoActionSuccess();

        String token = generateToken();
        EndpointResponse login = endpointApi.login(token);
        EndpointResponse action = endpointApi.action(token);
        EndpointResponse logout = endpointApi.logout(token);

        assertEquals("OK", login.getResult(), "LOGIN должен быть успешным");
        assertEquals("OK", action.getResult(), "ACTION должен быть успешным");
        assertEquals("OK", logout.getResult(), "LOGOUT должен быть успешным");
        wireMock().verify(exactly(1), postRequestedFor(urlEqualTo("/auth")));
        wireMock().verify(exactly(1), postRequestedFor(urlEqualTo("/doAction")));
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("Путь LOGIN -> LOGOUT -> ACTION")
    void shouldReturnErrorWhenTokenWasDeletedTest() {
        mockAuthSuccess();
        mockDoActionSuccess();

        String token = generateToken();
        EndpointResponse login = endpointApi.login(token);
        EndpointResponse logout = endpointApi.logout(token);
        EndpointResponse action = endpointApi.action(token);

        String expectedMessage = String.format("Token '%s' not found", token);

        assertEquals("OK", login.getResult(), "LOGIN должен быть успешным");
        assertEquals("OK", logout.getResult(), "LOGOUT должен быть успешным");
        assertEquals("ERROR", action.getResult(), "ACTION не должен быть успешным");
        assertEquals(expectedMessage, action.getMessage(), "Сообщение об ошибке должно быть");
        wireMock().verify(exactly(1), postRequestedFor(urlEqualTo("/auth")));
        wireMock().verify(exactly(0), postRequestedFor(urlEqualTo("/doAction")));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("Два токена не зависят друг от друга")
    void shouldWorkWith2TokensIndependentlyTest() {
        mockAuthSuccess();
        mockDoActionSuccess();

        String tokenA = generateToken();
        String tokenB = generateToken();
        String expectedMessageA = String.format("Token '%s' not found", tokenA);
        String expectedMessageB = String.format("Token '%s' not found", tokenB);

        EndpointResponse loginA = endpointApi.login(tokenA);
        EndpointResponse actionB = endpointApi.action(tokenB);
        EndpointResponse loginB = endpointApi.login(tokenB);
        EndpointResponse actionA = endpointApi.action(tokenA);
        EndpointResponse actionB2 = endpointApi.action(tokenB);
        EndpointResponse logoutA = endpointApi.logout(tokenA);
        EndpointResponse actionA2 = endpointApi.action(tokenA);
        EndpointResponse actionB3 = endpointApi.action(tokenB);

        assertEquals("OK", loginA.getResult(), "LOGIN для токена A должен быть успешным");
        assertEquals("ERROR", actionB.getResult(), "ACTION для токена B должен падать (токен еще не зарегистрирован)");
        assertEquals(expectedMessageB, actionB.getMessage(), "Сообщение об ошибке должно быть");
        assertEquals("OK", loginB.getResult(), "LOGIN для токена B должен быть успешным");
        assertEquals("OK", actionA.getResult(), "ACTION для токена A должен быть успешным");
        assertEquals("OK", actionB2.getResult(), "ACTION для токена B должен быть успешным");
        assertEquals("OK", logoutA.getResult(), "LOGOUT для токена A должен быть успешным");
        assertEquals("OK", actionB3.getResult(), "ACTION для токена B должен быть успешным");
        assertEquals("ERROR", actionA2.getResult(), "ACTION для токена A должен быть успешным (токен был удален)");
        assertEquals(expectedMessageA, actionA2.getMessage(), "Сообщение об ошибке должно быть");

        wireMock().verify(exactly(2), postRequestedFor(urlEqualTo("/auth")));
        wireMock().verify(exactly(3), postRequestedFor(urlEqualTo("/doAction")));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Owner("aqa")
    @DisplayName("ACTION можно вызывать многократно с одинаковым токеном")
    void should() {
        mockAuthSuccess();
        mockDoActionSuccess();

        String token = generateToken();

        EndpointResponse login = endpointApi.login(token);
        EndpointResponse action1 = endpointApi.action(token);
        EndpointResponse action2 = endpointApi.action(token);
        EndpointResponse action3 = endpointApi.action(token);

        assertEquals("OK", login.getResult(), "LOGIN должен быть успешным");
        assertEquals("OK", action1.getResult(), "ACTION 1 должен быть успешным");
        assertEquals("OK", action2.getResult(), "ACTION 2 должен быть успешным");
        assertEquals("OK", action3.getResult(), "ACTION 3 должен быть успешным");
        wireMock().verify(exactly(1), postRequestedFor(urlEqualTo("/auth")));
        wireMock().verify(exactly(3), postRequestedFor(urlEqualTo("/doAction")));

    }

}
