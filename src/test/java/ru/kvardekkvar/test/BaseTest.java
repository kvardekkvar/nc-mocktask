package ru.kvardekkvar.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.kvardekkvar.api.EndpointApi;
import ru.kvardekkvar.client.RestAssuredClientConfig;
import ru.kvardekkvar.extension.TestEnvironmentExtension;

@ExtendWith(TestEnvironmentExtension.class)
public abstract class BaseTest {
    protected EndpointApi endpointApi = new EndpointApi();

    @BeforeEach
    void setUp() {
        RestAssuredClientConfig.configure();
    }

}
