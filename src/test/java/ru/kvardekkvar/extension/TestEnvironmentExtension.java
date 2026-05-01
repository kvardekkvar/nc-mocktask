package ru.kvardekkvar.extension;

import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.TestAbortedException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static ru.kvardekkvar.config.TestConfig.API_KEY;
import static ru.kvardekkvar.config.TestConfig.APP_HOST;
import static ru.kvardekkvar.config.TestConfig.APP_JAR_PATH;
import static ru.kvardekkvar.config.TestConfig.APP_PORT;
import static ru.kvardekkvar.config.TestConfig.MOCK_PORT;

public class TestEnvironmentExtension implements BeforeAllCallback, AfterAllCallback {
    private static Process appProcess;

    @Override
    public void beforeAll(ExtensionContext context) {
        startApplication();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        stopApplication();
    }


    private void startApplication() {
        File appJar = new File(APP_JAR_PATH);
        if (!appJar.exists()) {
            throw new TestAbortedException(
                    "Jar-файл тестируемого приложения не найден. Положите его в папку app/ или укажите ключ -Dapp.jar=<path>. Текущее значение: "
                            + APP_JAR_PATH);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(
                "java",
                "-jar",
                "-Dsecret=" + API_KEY,
                "-Dmock=http://localhost:" + MOCK_PORT,
                appJar.getAbsolutePath()
        );
        processBuilder.redirectErrorStream(true);

        try {
            appProcess = processBuilder.start();
            waitUntilAppIsReady(Duration.ofSeconds(20));
        } catch (IOException e) {
            throw new TestAbortedException("Не удалось запустить тестируемое приложение", e);
        }
    }

    @SneakyThrows
    private void waitUntilAppIsReady(Duration timeout) {
        Instant deadline = Instant.now().plus(timeout);
        while (Instant.now().isBefore(deadline)) {
            try {
                HttpURLConnection connection = (HttpURLConnection)
                        new URL(APP_HOST + ":" + APP_PORT + "/endpoint").openConnection();
                connection.setConnectTimeout(500);
                connection.setReadTimeout(500);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.getResponseCode();
                return;
            } catch (IOException ignored) {
                if (appProcess != null && !appProcess.isAlive()) {
                    throw new TestAbortedException("Application process terminated before startup");
                }
                Thread.sleep(300);
            }
        }
        throw new TestAbortedException("Application did not become ready within " + timeout.toSeconds() + " seconds");
    }

    private void stopApplication() {
        if (appProcess == null) {
            return;
        }
        appProcess.destroy();
        try {
            if (!appProcess.waitFor(5, TimeUnit.SECONDS)) {
                appProcess.destroyForcibly();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            appProcess.destroyForcibly();
        }
    }
}
