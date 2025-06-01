# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Development Commands

This is a Maven-based Spring Boot application using Java 21.

### Build Commands
- `./mvnw clean compile` - Compile the application
- `./mvnw clean package` - Build JAR package
- `./mvnw spring-boot:run` - Run the application locally (port 5920)

### Testing
- `./mvnw test` - Run all tests
- `./mvnw test -Dtest=ClassName` - Run specific test class

## Architecture Overview

The Aligned Execution Engine is a Spring Boot application that serves as both an MCP (Model Context Protocol) server and an OpenAI integration service. The application provides controlled execution of user prompts through different interfaces.

### Core Components

**MCP Server Integration** (`PromptExecutorMcpService`):
- Exposes an `execute` tool via Spring AI's MCP server framework
- Routes user requests based on keyword matching (bank, purchase, weather, apple)
- Returns hardcoded responses for demonstration purposes

**HTTP API** (`TestActionController`):
- Provides REST endpoint `/test-controller-agent` for testing OpenAI integration
- Delegates to `ActionControllerService` for AI-powered responses

**OpenAI Integration** (`ActionControllerService`):
- Uses Spring AI's ChatClient to interact with GPT-4o
- Configured with a "bohemian poet" system prompt that creates song responses
- Temperature set to 0.5 for balanced creativity

### Configuration

**Application Properties**:
- Server runs on port 5920
- MCP server enabled with sync protocol
- OpenAI API key sourced from `AEE_OPENAI_API_KEY` environment variable

**Dependencies**:
- Spring Boot 3.4.5 with Spring AI 1.0.0-M8
- MCP server support via `spring-ai-starter-mcp-server-webmvc`
- OpenAI integration via `spring-ai-starter-model-openai`
- Lombok for code generation

### Environment Setup

Set the required environment variable:
```bash
export AEE_OPENAI_API_KEY=your_openai_api_key
```

## MCP Client Integration

The application includes MCP (Model Context Protocol) client functionality to connect to external MCP servers via server-sent events.

### MCP Client Architecture

**Key Learning**: MCP tools should NOT be called directly through `McpSyncClient`. Instead, Spring AI provides an abstraction layer that integrates MCP tools with AI models through `ChatClient` and `ToolCallbackProvider`.

**MCP Client Service** (`McpClientService`):
- Uses `ChatClient` configured with MCP tool callbacks
- AI model automatically discovers and calls MCP tools based on conversation context
- Example: Asking "check my balance" triggers the `check-balance` MCP tool

**MCP Client Configuration** (`McpClientConfig`):
- Creates dedicated `ChatClient` bean for MCP operations
- Configures `ToolCallbackProvider` to expose MCP tools to the AI model
- Separate from the main OpenAI ChatClient to avoid conflicts

### MCP Configuration Properties

```properties
# Enable MCP client with tool callback integration
spring.ai.mcp.client.enabled=true
spring.ai.mcp.client.type=SYNC
spring.ai.mcp.client.toolcallback.enabled=true
spring.ai.mcp.client.request-timeout=20s

# SSE connection to external MCP server
spring.ai.mcp.client.sse.connections.acme-bank.url=https://incode-acme-mcp.ngrok.app/sse
```

### MCP Dependencies

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-client</artifactId>
</dependency>
```

### Testing MCP Integration

- **Endpoint**: `GET /test-mcp-client`
- **Purpose**: Tests MCP client integration by asking AI to call `check-balance` tool
- **Architecture**: User request → ChatClient → AI model → MCP tool discovery → Tool execution → Response