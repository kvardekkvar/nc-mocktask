package ru.kvardekkvar.config;

public final class TestConfig {
    public static final String APP_HOST = System.getProperty("app.host", "http://localhost");
    public static final int APP_PORT = Integer.getInteger("app.port", 8080);
    public static final String MOCK_HOST = System.getProperty("mock.host", "localhost");
    public static final int MOCK_PORT = Integer.getInteger("mock.port", 8888);
    public static final String API_KEY = System.getProperty("api.key", "qazWSXedc");
    public static final String APP_JAR_PATH = System.getProperty("app.jar", "app/internal-0.0.1-SNAPSHOT.jar");
}
