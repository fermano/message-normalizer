# Message Normalizer

This project provides a simple Spring Boot application exposing two HTTP POST endpoints to receive messages from different providers. Incoming messages are converted into a standardized format and then sent to a mocked queue (logged to the console).

## Requirements
- Java 17+
- Maven 3+

## Building
```
mvn clean package
```

## Running
```
mvn spring-boot:run
```

The application starts on port `8080`.

## Endpoints
- `POST /provider-alpha/feed`
- `POST /provider-beta/feed`

Both endpoints accept a JSON body with either an **odds update** or **settlement** message according to each provider's format.

The service logs the standardized message that would be placed on a queue.
