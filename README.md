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

## Testing Data

### ProviderAlpha (Endpoint: `/provider-alpha/feed`)

#### ODDS_CHANGE message
```json
{
  "msg_type": "odds_update",
  "event_id": "ev123",
  "values": {
    "1": 2.0,
    "X": 3.1,
    "2": 3.8
  }
}
```

#### BET_SETTLEMENT message
```json
{
  "msg_type": "settlement",
  "event_id": "ev123",
  "outcome": "1" // One of "1", "X", or "2"
}
```

---

### ProviderBeta (Endpoint: `/provider-beta/feed`)

#### ODDS_CHANGE message
```json
{
  "type": "ODDS",
  "event_id": "ev456",
  "odds": {
    "home": 1.95,
    "draw": 3.2,
    "away": 4.0
  }
}
```

#### BET_SETTLEMENT message
```json
{
  "type": "SETTLEMENT",
  "event_id": "ev456",
  "result": "away" // One of "home", "draw", or "away"
}
```
