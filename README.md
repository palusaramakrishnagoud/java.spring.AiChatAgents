# AI Chat Agent

Spring Boot service that uses [Spring AI](https://docs.spring.io/spring-ai/reference/) with LLM providers for interaction.

## Stack

- Java 21
- Spring Boot 3.4.1
- Spring AI 1.0.0-M5 (`spring-ai-anthropic-spring-boot-starter`)
- Apache HttpClient 5 (used by `AIChatService` for a direct Anthropic HTTP client)
- Maven Wrapper

Default model: claude-haiku-4-5( Anthropic), mistral:7b (Ollama)  
Default port: `8081`

## Prerequisites

- JDK 21+
- An [Anthropic API key](https://console.anthropic.com/)

## Configuration

The app reads the Anthropic key from the `ANTHROPIC_API_KEY` environment variable (`application.properties` maps it to `spring.ai.anthropic.api-key`).

**Windows (PowerShell)**

```powershell
$env:ANTHROPIC_API_KEY = "your-api-key"
```

**macOS / Linux**

```bash
export ANTHROPIC_API_KEY=your-api-key
```

Other settings in `aichatagent/src/main/resources/application.properties`:

| Property | Value |
| --- | --- |
| `spring.application.name` | `agentmain` |
| `server.port` | `8081` |
| `spring.ai.anthropic.chat.options.model` | `claude-haiku-4-5` |

Logs go to the console (INFO). Logback is configured in `aichatagent/src/main/resources/logback-spring.xml`.

## Run

From `aichatagent/`:

```bash
./mvnw spring-boot:run
```

On Windows:

```cmd
mvnw.cmd spring-boot:run
```

Or build a jar:

```bash
./mvnw clean package
java -jar target/aichatagent-0.0.1-SNAPSHOT.jar
```

The HTTP API is served at `http://localhost:8081`.

## API

Both endpoints are `POST` and take query parameters. They use the Spring AI `ChatClient` bean named `anthropicChatClient`.

### Summarize a message

`POST /api/anthropicai/chat/summary`

```bash
curl -X POST "http://localhost:8081/api/anthropicai/chat/summary?message=Key%20points%20from%20the%20sprint%20review"
```

### Summarize meeting notes

`POST /api/anthropicai/chat/summarize-notes`

```bash
curl -X POST "http://localhost:8081/api/anthropicai/chat/summarize-notes?notes=We%20reviewed%20Q3%20targets%20and%20proposed%20two%20new%20regions."
```

Responses are plain text from the model.

## Project layout

```
aichatagent/
  pom.xml
  src/main/java/com/genai/java/spring/
    Application.java
    Config/AIChatProviderConfig.java    # ChatClient bean
    Controller/AIChatController.java    # REST endpoints
    Service/AIChatService.java          # Direct Anthropic HTTP client
    AIChatExceptions/AIChatException.java
  src/main/resources/
    application.properties
    logback-spring.xml
  src/test/java/com/genai/java/spring/ApplicationTests.java
```

`AIChatProviderConfig` builds a `ChatClient` from Spring AI’s `AnthropicChatModel`. The controller uses that client. `AIChatService` is a separate Apache HttpClient-based caller and is not wired into the controller yet.

## Tests (WIP)

```bash
./mvnw test
```

`ApplicationTests` currently checks that the Spring context starts.
