# AQA Mock Task (Java 17)

Проект с автотестами для приложения из тестового задания.

## Стек

- Java 17
- Maven
- JUnit 5
- WireMock
- Allure
- Rest Assured

## Как запускать

1. Положить тестируемый `jar` в папку app 
   или передать путь через `-Dapp.jar=<path_to_jar>`.
2. Запустить тесты:

```bash
mvn clean test
```

3. Сгенерировать отчет
```bash
mvn allure:serve
```
