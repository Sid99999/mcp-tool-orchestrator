# MCP Tool Orchestrator

A Java 21 + Spring Boot personal portfolio project demonstrating a production-style Model Context Protocol (MCP) tool gateway.

## Features
- Stateless MCP Streamable HTTP server
- `@McpTool`-based tool discovery and execution
- Tenant-aware tool execution
- Redis-backed per-tenant/per-tool rate limiting
- Resilience4j circuit breaker around a downstream order service
- PostgreSQL audit trail for tool invocations
- Latency/outcome capture
- Docker Compose for Redis + PostgreSQL
- Separation between MCP adapter layer and backend orchestration services

## Architecture

```text
AI / MCP Client
      |
      | Streamable HTTP
      v
Spring AI MCP Server (/mcp)
      |
      v
MCP Tool Layer
      |
      v
Tool Orchestration
  |       |       |
 Redis  Postgres  Resilience4j
  |       |       |
 rate    audit   downstream
 limit   log     order service
```

## Run locally

Prerequisites: Java 21, Maven 3.9+, Docker.

```bash
docker compose up -d
mvn spring-boot:run
```

Health: `http://localhost:8080/api/health`

MCP endpoint: `http://localhost:8080/mcp`

## Tools

- `get_order_status(tenantId, clientId, orderId)`
- `search_orders(tenantId, clientId, status)`
- `create_support_ticket(tenantId, clientId, issue)`

## Portfolio positioning

This project is designed to demonstrate MCP as a backend platform boundary rather than a toy AI demo: tool discovery/execution, rate limiting, tenant isolation, resilience and auditability.

## Resume note

Use this as a personal project only after you have run and tested it. Do not describe it as Swiss Re production experience or add performance numbers that you have not benchmarked.
