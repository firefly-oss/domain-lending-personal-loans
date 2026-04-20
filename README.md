# domain-lending-personal-loans

> Domain-layer orchestration microservice for personal loan workflows, covering the lifecycle of personal loan agreements.

## Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Module Structure](#module-structure)
- [API Endpoints](#api-endpoints)
- [Domain Logic](#domain-logic)
- [Dependencies](#dependencies)
- [Configuration](#configuration)
- [Running Locally](#running-locally)
- [Testing](#testing)

## Overview

`domain-lending-personal-loans` is a reactive Spring Boot microservice that acts as the domain-layer orchestrator for personal loan operations within the Firefly lending platform. It sits between experience-layer consumers (mobile, web, BFF services) and the core service `core-lending-personal-loans`, coordinating all business operations through a CQRS command/query bus.

The service manages the lifecycle of personal loan agreements — including agreement creation, retrieval, update, and listing. All interactions are fully reactive, built on Spring WebFlux and Project Reactor.

Rather than owning persistent state, this service delegates every write and read operation to `core-lending-personal-loans` via its generated WebClient-based SDK (`core-lending-personal-loans-sdk`). This clean separation keeps domain orchestration logic decoupled from persistence concerns and ensures a single source of truth for personal loan data.

## Architecture

The service follows the **CQRS (Command Query Responsibility Segregation)** pattern provided by `fireflyframework-starter-domain`. Incoming HTTP requests are translated into typed `Command` or `Query` objects, dispatched through a `CommandBus` or `QueryBus`, and handled by dedicated handler classes that call the downstream core service SDK.

```
Experience Layer (BFF / Mobile / Web)
            |
            v
  [ domain-lending-personal-loans ]  :8044
            |
     PersonalLoansController
            |
     PersonalLoansServiceImpl
            |
    CommandBus / QueryBus  (fireflyframework CQRS)
            |
    Command/Query Handlers
            |
     core-lending-personal-loans-sdk  (WebClient)
            |
            v
  [ core-lending-personal-loans ]  :8091
```

Event-driven integration is enabled via `fireflyframework-eda` with Kafka as the default publisher. Step events are also enabled to allow the framework to emit execution telemetry. CQRS commands carry a 30-second timeout; queries carry a 15-second timeout with a 15-minute query cache TTL.

## Module Structure

| Module | Purpose |
|--------|---------|
| `domain-lending-personal-loans-interfaces` | Domain-level DTOs exposed to experience-layer consumers (e.g., `PersonalLoanAgreementSummaryDTO`). Does not own persistence; depends on `core`. |
| `domain-lending-personal-loans-core` | CQRS commands, queries, command/query handlers, and the `PersonalLoansService` interface with its implementation. All downstream SDK calls originate here. |
| `domain-lending-personal-loans-infra` | Infrastructure concerns: `PersonalLoansClientFactory` (instantiates WebClient-based SDK API beans) and `PersonalLoansProperties` (binds `api-configuration.core-lending.personal-loans` config). |
| `domain-lending-personal-loans-web` | Spring Boot application entry point, `PersonalLoansController` (REST layer), OpenAPI/Swagger configuration, and web-layer unit tests. |
| `domain-lending-personal-loans-sdk` | Auto-generated client SDK for this service, produced by the OpenAPI Generator Maven plugin from the service's own OpenAPI spec. Consumed by experience-layer services. |

## API Endpoints

All endpoints are prefixed with `/api/v1/personal-loans` and produce/consume `application/json`. All path variables that identify resources are UUIDs.

### Agreements

| Method | Path | Description | Success Status |
|--------|------|-------------|----------------|
| `POST` | `/agreements` | Create a new personal loan agreement | `201 Created` |
| `GET` | `/agreements/{agreementId}` | Retrieve an agreement by ID | `200 OK` / `404 Not Found` |
| `PUT` | `/agreements/{agreementId}` | Update an existing agreement | `200 OK` / `404 Not Found` |
| `GET` | `/agreements` | List all personal loan agreements (paginated) | `200 OK` |

OpenAPI documentation (Swagger UI) is available at `/swagger-ui.html` and the raw spec at `/v3/api-docs` (disabled in production profile).

## Domain Logic

The domain service layer (`PersonalLoansServiceImpl`) is a pure dispatcher: it constructs typed command or query objects and sends them to the appropriate bus. All business logic resides in the individual handlers.

### Command Handlers

Each command handler extends `CommandHandler<C, R>` and is annotated with `@CommandHandlerComponent` so the CQRS framework auto-registers it. Every write command passes a freshly generated `UUID` as an idempotency key to the downstream SDK.

| Handler | Command | Downstream SDK Call |
|---------|---------|---------------------|
| `CreatePersonalLoanAgreementHandler` | `CreatePersonalLoanAgreementCommand(dto)` | `PersonalLoanAgreementApi.create(dto, idempotencyKey)` |
| `UpdatePersonalLoanAgreementHandler` | `UpdatePersonalLoanAgreementCommand(agreementId, dto)` | `PersonalLoanAgreementApi.update(agreementId, dto, idempotencyKey)` |

### Query Handlers

Each query handler extends `QueryHandler<Q, R>` and is annotated with `@QueryHandlerComponent`. Queries are subject to the 15-minute cache TTL configured on the query bus.

| Handler | Query | Downstream SDK Call |
|---------|-------|---------------------|
| `GetPersonalLoanAgreementHandler` | `GetPersonalLoanAgreementQuery(agreementId)` | `PersonalLoanAgreementApi.getById(agreementId, idempotencyKey)` |
| `ListPersonalLoanAgreementsHandler` | `ListPersonalLoanAgreementsQuery()` | `PersonalLoanAgreementApi.findAll(filter, idempotencyKey)` |

### SDK Models

Commands and queries carry the following DTOs sourced from `core-lending-personal-loans-sdk`:

- `PersonalLoanAgreementDTO` — personal loan agreement record (loan purpose, status, rate type, interest rate, dates, loan servicing case link)
- `PaginationResponse` — paginated wrapper for list results
- `FilterRequestPersonalLoanAgreementDTO` — filter criteria for listing agreements

The `interfaces` module also exposes `PersonalLoanAgreementSummaryDTO`, a lighter projection of agreement data intended for experience-layer consumers that do not need the full core DTO.

## Dependencies

### Upstream (services this service calls)

| Service | Artifact | Purpose |
|---------|----------|---------|
| `core-lending-personal-loans` | `core-lending-personal-loans-sdk` | Persistence and business rules for personal loan agreements. Default base path: `http://localhost:8091` (override via `PERSONAL_LOANS_CORE_URL`). |

The infra module instantiates a WebClient-based SDK API bean through `PersonalLoansClientFactory`:

- `PersonalLoanAgreementApi`

### Downstream (services that consume this service)

Experience-layer services and BFF applications consume this service's REST API. The `domain-lending-personal-loans-sdk` module provides a generated WebClient-based client for those consumers, built from this service's OpenAPI spec using the OpenAPI Generator Maven plugin (`openapi-generator-maven-plugin` 7.0.1, `webclient` library, reactive mode).

### Framework Libraries

| Library | Purpose |
|---------|---------|
| `fireflyframework-starter-domain` | CQRS bus infrastructure (`CommandBus`, `QueryBus`, `@CommandHandlerComponent`, `@QueryHandlerComponent`) |
| `fireflyframework-web` | Global exception handling, error response negotiation |
| `fireflyframework-utils` | Shared utilities |
| `fireflyframework-validators` | Common validation support |
| `spring-boot-starter-webflux` | Reactive HTTP server (Netty) |
| `spring-boot-starter-actuator` | Health, info, and Prometheus metrics endpoints |
| `springdoc-openapi-starter-webflux-ui` | Swagger UI and OpenAPI spec generation |
| `micrometer-registry-prometheus` | Prometheus metrics export |
| `mapstruct` | Compile-time bean mapping |
| `lombok` | Boilerplate reduction |

## Configuration

All configuration is in `domain-lending-personal-loans-web/src/main/resources/application.yaml`.

### Core Settings

| Property | Default | Description |
|----------|---------|-------------|
| `spring.application.name` | `domain-lending-personal-loans` | Service name |
| `spring.application.version` | `1.0.0` | Service version |
| `spring.threads.virtual.enabled` | `true` | Enables Java virtual threads |
| `server.address` | `localhost` (env: `SERVER_ADDRESS`) | Bind address |
| `server.port` | `8044` (env: `SERVER_PORT`) | HTTP listen port |
| `server.shutdown` | `graceful` | Graceful shutdown on SIGTERM |

### Downstream Service URL

| Property | Default | Description |
|----------|---------|-------------|
| `api-configuration.core-lending.personal-loans.base-path` | `http://localhost:8091` (env: `PERSONAL_LOANS_CORE_URL`) | Base URL of the `core-lending-personal-loans` service. Bound to `PersonalLoansProperties`. |

### CQRS

| Property | Value | Description |
|----------|-------|-------------|
| `firefly.cqrs.enabled` | `true` | Enables the CQRS framework |
| `firefly.cqrs.command.timeout` | `30s` | Maximum duration for command execution |
| `firefly.cqrs.command.metrics-enabled` | `true` | Emit Micrometer metrics for commands |
| `firefly.cqrs.command.tracing-enabled` | `true` | Enable distributed tracing for commands |
| `firefly.cqrs.query.timeout` | `15s` | Maximum duration for query execution |
| `firefly.cqrs.query.caching-enabled` | `true` | Enable query result caching |
| `firefly.cqrs.query.cache-ttl` | `15m` | Query cache time-to-live |
| `firefly.saga.performance.enabled` | `true` | Enable saga performance monitoring |

### Event-Driven Architecture (EDA)

| Property | Value | Description |
|----------|-------|-------------|
| `firefly.eda.enabled` | `true` | Enable event publishing |
| `firefly.eda.default-publisher-type` | `KAFKA` | Default publisher backend |
| `firefly.eda.default-connection-id` | `default` | Connection profile to use |
| `firefly.eda.publishers.kafka.default.enabled` | `true` | Activate the default Kafka publisher |
| `firefly.eda.publishers.kafka.default.default-topic` | `domain-layer` | Kafka topic for domain events |
| `firefly.eda.publishers.kafka.default.bootstrap-servers` | `localhost:9092` | Kafka broker address |
| `firefly.stepevents.enabled` | `true` | Emit CQRS step execution events |

### Actuator / Observability

| Property | Value | Description |
|----------|-------|-------------|
| `management.endpoints.web.exposure.include` | `health,info,prometheus` | Exposed actuator endpoints |
| `management.endpoint.health.show-details` | `when-authorized` | Show health details only to authorized callers |
| `management.health.livenessState.enabled` | `true` | Kubernetes liveness probe support |
| `management.health.readinessState.enabled` | `true` | Kubernetes readiness probe support |

### OpenAPI / Swagger UI

| Property | Value | Description |
|----------|-------|-------------|
| `springdoc.api-docs.path` | `/v3/api-docs` | OpenAPI JSON spec path |
| `springdoc.swagger-ui.path` | `/swagger-ui.html` | Swagger UI path |
| `springdoc.packages-to-scan` | `com.firefly.domain.lending.personalloans.web.controllers` | Packages scanned for API definitions |
| `springdoc.paths-to-match` | `/api/**` | URL patterns included in the spec |

Swagger UI and the API docs endpoint are **disabled in the `prod` profile**.

### Logging Profiles

| Profile | Root Level | `com.firefly` Level | `org.springframework` Level |
|---------|-----------|---------------------|-----------------------------|
| _(default)_ | — | — | — |
| `dev` | `INFO` | `DEBUG` | — |
| `pre` | `INFO` | `INFO` | — |
| `prod` | `WARN` | `INFO` | `WARN` |

## Running Locally

Ensure `core-lending-personal-loans` is running on `http://localhost:8091` and a Kafka broker is available on `localhost:9092` before starting this service.

```bash
mvn clean install -DskipTests
cd /Users/ancongui/Development/firefly/domain-lending-personal-loans
mvn spring-boot:run -pl domain-lending-personal-loans-web
```

To override the downstream service URL or port at startup:

```bash
PERSONAL_LOANS_CORE_URL=http://my-core-service:8091 SERVER_PORT=8044 mvn spring-boot:run -pl domain-lending-personal-loans-web
```

Server port: **8044**

Swagger UI (non-production): [http://localhost:8044/swagger-ui.html](http://localhost:8044/swagger-ui.html)

Health check: [http://localhost:8044/actuator/health](http://localhost:8044/actuator/health)

Prometheus metrics: [http://localhost:8044/actuator/prometheus](http://localhost:8044/actuator/prometheus)

## Testing

The web layer is tested with `@WebFluxTest` and `WebTestClient` in `PersonalLoansControllerTest`. The `PersonalLoansService` is mocked with Mockito; the `fireflyframework-web` global exception handler beans are also mocked as required by the test slice. Tests cover HTTP status codes for happy paths (201 Created, 200 OK) and not-found scenarios (404) across agreements.

```bash
mvn clean verify
```

To run only the web module tests:

```bash
mvn clean verify -pl domain-lending-personal-loans-web
```
