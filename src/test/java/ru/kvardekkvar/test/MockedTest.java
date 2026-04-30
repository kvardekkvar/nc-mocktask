package ru.kvardekkvar.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static ru.kvardekkvar.config.TestConfig.MOCK_HOST;
import static ru.kvardekkvar.config.TestConfig.MOCK_PORT;

public class MockedTest extends BaseTest {

    static WireMockServer wireMockServer;

    @BeforeEach
    void resetMocks() {
        wireMockServer.resetAll();
    }
    @BeforeAll
    static void start() {
        wireMockServer = new WireMockServer(MOCK_PORT);
        wireMockServer.start();
        WireMock.configureFor(MOCK_HOST, MOCK_PORT);
    }

    @AfterAll
    static void stop() {
        if (wireMockServer == null) {
            throw new IllegalStateException("WireMock server is not started");
        }
        wireMockServer.stop();
    }

    protected WireMockServer wireMock() {
        return wireMockServer;
    }

}
