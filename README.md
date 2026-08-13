# MCP Tool Orchestrator

A Java 21 and Spring Boot backend that exposes business capabilities through the Model Context Protocol (MCP), with tenant-aware execution, rate limiting, resilience, and auditability.

## Overview

The MCP Tool Orchestrator acts as a backend boundary between MCP clients and business services.

Instead of allowing AI clients to directly interact with individual backend services, the platform exposes controlled tools through a standardized MCP interface. Each tool invocation passes through rate limiting, orchestration, downstream service protection, and audit logging.

## Architecture

```text
                 AI / MCP Client
                       |
                       | Streamable HTTP
                       v
              Spring AI MCP Server
                       |
                       v
                 MCP Tool Layer
                       |
                       v
              Tool Orchestration
              /        |        \
             /         |         \
          Redis     PostgreSQL   Resilience4j
            |            |            |
       Rate Limit      Audit       Circuit
       per Tenant      Trail       Breaker
                                      |
                                      v
                              Downstream Services
```

## Key Features

- **MCP Server** using Spring AI and Streamable HTTP
- **Tool discovery and execution** using `@McpTool`
- **Tenant-aware tool execution** using tenant and client context
- **Redis-backed rate limiting** per tenant and tool
- **Resilience4j circuit breaker** for downstream service protection
- **PostgreSQL audit logging** for tool invocations
- **Latency and outcome tracking** for each invocation
- Separation between the **MCP adapter layer** and backend orchestration services
- Docker Compose configuration for local infrastructure

## Available Tools

### `get_order_status`

Retrieves the status of an order from the downstream order service.

```text
get_order_status(
    tenantId,
    clientId,
    orderId
)
```

### `search_orders`

Searches orders based on their current status.

```text
search_orders(
    tenantId,
    clientId,
    status
)
```

### `create_support_ticket`

Creates a support ticket for a customer issue.

```text
create_support_ticket(
    tenantId,
    clientId,
    issue
)
```

## Reliability

Tool invocations are protected using multiple backend mechanisms.

### Rate Limiting

Redis maintains per-tenant and per-tool request limits to prevent excessive or uncontrolled tool execution.

```text
tenant-a + get_order_status
tenant-a + search_orders
tenant-b + get_order_status
```

Each combination maintains its own rate-limit context.

### Circuit Breaking

Resilience4j protects calls to downstream services from repeated failures.

```text
MCP Tool
   |
   v
Circuit Breaker
   |
   +---- Healthy ----> Downstream Service
   |
   +---- Failing ----> Circuit Open
```

This prevents repeated calls to an unhealthy downstream dependency.

### Auditability

Tool invocations are persisted in PostgreSQL with information such as:

- Tenant
- Client
- Tool name
- Latency
- Execution outcome
- Timestamp

This provides visibility into tool usage and execution behaviour.

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot |
| AI Protocol | Spring AI MCP |
| Cache / Rate Limiting | Redis |
| Database | PostgreSQL |
| Resilience | Resilience4j |
| Containerization | Docker |
| Build Tool | Maven |

## Running Locally

### Prerequisites

- Java 21
- Maven 3.9+
- Docker

### Start Infrastructure

```bash
docker compose up -d
```

This starts the required Redis and PostgreSQL services.

### Start the Application

```bash
mvn spring-boot:run
```

The application starts on:

```text
http://localhost:8080
```

### Health Check

```text
http://localhost:8080/api/health
```

### MCP Endpoint

```text
http://localhost:8080/mcp
```

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── ...
│   └── resources/
│       └── application.yml
└── test/
    └── ...

docker-compose.yml
pom.xml
README.md
```

## Design Goals

The project focuses on treating MCP as a **backend platform boundary** rather than simply exposing a chatbot.

The main engineering concerns are:

- Controlled tool execution
- Tenant isolation
- Rate limiting
- Downstream resilience
- Auditability
- Observability
- Separation of protocol and business logic
