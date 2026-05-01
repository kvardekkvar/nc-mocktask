package ru.kvardekkvar.test;

import org.junit.jupiter.api.extension.ExtendWith;
import ru.kvardekkvar.api.EndpointApi;
import ru.kvardekkvar.extension.TestEnvironmentExtension;

@ExtendWith(TestEnvironmentExtension.class)
public abstract class BaseTest {
    protected EndpointApi endpointApi = new EndpointApi();

}
