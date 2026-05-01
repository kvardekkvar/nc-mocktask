# AQA Mock Task

Проект с автотестами для приложения из тестового задания.

## Стек

- Java 17
- Maven
- JUnit 5
- WireMock
- Allure
- Rest Assured

## Для запуска необходимы:

- **[Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)**
- **[Apache Maven 3.6+](https://maven.apache.org/install.html)**

## Как запускать

1. Положить тестируемый `jar` в папку app
   или передать путь через `-Dapp.jar=<path_to_jar>`.
2. Открыть командную строку в корневой директории проекта
3. Запустить тесты:

```bash
mvn clean test
```

3. Сгенерировать отчет

```bash
mvn allure:serve
```
