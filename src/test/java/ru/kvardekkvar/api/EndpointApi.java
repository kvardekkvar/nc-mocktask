package ru.kvardekkvar.api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.kvardekkvar.client.RestAssuredClient;
import ru.kvardekkvar.model.endpoint.EndpointRequest;
import ru.kvardekkvar.model.endpoint.EndpointResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static ru.kvardekkvar.client.RestAssuredClient.defaultSpec;
import static ru.kvardekkvar.config.TestConfig.API_KEY;

public class EndpointApi {

    private final RestAssuredClient client = new RestAssuredClient();

    @Step("Зарегистрировать токен {token}")
    public EndpointResponse login(String token) {
        return send(new EndpointRequest(token, "LOGIN"));
    }

    @Step("Выполнить действие с токеном {token}")
    public EndpointResponse action(String token) {
        return send(new EndpointRequest(token, "ACTION"));
    }

    @Step("Удалить токен {token}")
    public EndpointResponse logout(String token) {
        return send(new EndpointRequest(token, "LOGOUT"));
    }


    @Step("Отправить запрос POST /endpoint с действием {request.action} с токеном {request.token}")
    public EndpointResponse send(EndpointRequest request) {
        return send(request, API_KEY);
    }

    @Step("Отправить запрос на /endpoint без ключа авторизации")
    public EndpointResponse sendWithoutApiKey(EndpointRequest request) {
        return send(request, null);
    }

    @Step("Отправить запрос на /endpoint с указанным ключом авторизации")
    public EndpointResponse sendWithApiKey(EndpointRequest request, String apiKey) {
        return send(request, apiKey);
    }

    private EndpointResponse send(EndpointRequest request, String apiKey) {
        Map<String, String> body = new HashMap<>();
        Optional.ofNullable(request.getToken()).ifPresent(v -> body.put("token", v));
        Optional.ofNullable(request.getAction()).ifPresent(v -> body.put("action", v));

        return client.post("/endpoint",
                ContentType.URLENC,
                ContentType.JSON,
                apiKey,
                body,
                EndpointResponse.class
        );

    }
}
