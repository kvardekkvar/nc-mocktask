package ru.kvardekkvar.test.endpoint;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kvardekkvar.model.endpoint.EndpointRequest;
import ru.kvardekkvar.model.endpoint.EndpointResponse;
import ru.kvardekkvar.test.BaseTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("Ручка /endpoint")
@Feature("Валидация параметров")
@DisplayName("/endpoint — Валидация")
class EndpointValidationTest extends BaseTest {

    private static final String INVALID_TOKEN_MESSAGE = "token: должно соответствовать \\\"^[0-9A-Z]{32}$\\\"";
    private static final String EMPTY_TOKEN_MESSAGE = "token: не должно равняться null";
    private static final String INVALID_ACTION_MESSAGE = "action: invalid action '%s'. Allowed: LOGIN, LOGOUT, ACTION";

    @ParameterizedTest(name = "{0}")
    @MethodSource("validationCases")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("aqa")
    @DisplayName("Валидация тела запроса /endpoint: ")
    void shouldValidateTokenAndAction(
            String scenarioName,
            String token,
            String action,
            String expectedResult,
            String expectedMessage) {
        EndpointResponse response = endpointApi.send(new EndpointRequest(token, action));

        assertEquals(expectedResult, response.getResult(), "Результат должен соответствовать сценарию");
        assertEquals(expectedMessage, response.getMessage(), "Должна быть корректная причина ошибки");
    }

    private static Stream<Arguments> validationCases() {
        return Stream.of(
                Arguments.of("token короче 32 символов", "ABC123", "LOGIN", "ERROR", INVALID_TOKEN_MESSAGE),
                Arguments.of("token длиннее 32 символов", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "ACTION", "ERROR", INVALID_TOKEN_MESSAGE),
                Arguments.of("token содержит строчные буквы", "AAAAAAAAAAAAAAAAAAAAAAAaaaaaAAAA", "LOGOUT", "ERROR", INVALID_TOKEN_MESSAGE),
                Arguments.of("token содержит спецсимволы", "AAAAAAAAAAAAAAAAAAAAAAA@@@@@!№!(", "LOGIN", "ERROR", INVALID_TOKEN_MESSAGE),
                Arguments.of("token пустой", null, "LOGIN", "ERROR", EMPTY_TOKEN_MESSAGE),
                Arguments.of("action — неизвестное значение", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "UNKNOWN", "ERROR", String.format(INVALID_ACTION_MESSAGE, "UNKNOWN")),
                Arguments.of("action — пустая строка", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", null, "ERROR", String.format(INVALID_ACTION_MESSAGE, "null"))
        );
    }
}
