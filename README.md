# BardenasKutxa

A Scala-based application for managing orders and payments in a "sociedad gastronómica" (gastronomic society).

## Overview

BardenasKutxa is built using functional programming principles with Scala and the Cats Effect library. The application provides a REST API for managing products, orders, and payments in a gastronomic society setting.

## Architecture

The project follows **Clean Architecture** principles with a clear separation of concerns:

```
src/main/scala/com/mikelalvarezgo/socikutxa/
├── App.scala                    # Main application entry point
├── product/                     # Product bounded context
│   ├── application/            # Use cases (ImportProductsUseCase)
│   ├── domain/                 # Domain models (Product, ProductId, ProductCategory)
│   └── infrastructure/         # Controllers, repositories, and context
└── shared/                     # Shared kernel
    ├── domain/                 # Shared domain contracts and errors
    ├── infrastructure/         # Configuration, persistence, monitoring
    └── utils/                  # Utility functions
```

### Key Components

- **Domain Layer**: Pure business logic with domain models and repository contracts
- **Application Layer**: Use cases orchestrating domain logic
- **Infrastructure Layer**: HTTP controllers, database repositories, and external integrations

## Tech Stack

- **Scala 2.13.15**
- **Cats Effect** - Functional effects and concurrency
- **Http4s** - HTTP server and client (Ember)
- **Doobie** - Functional database access
- **PostgreSQL** - Primary data store
- **Flyway** - Database migrations
- **Kamon** - Application monitoring and metrics

## Prerequisites

- JDK 11 or higher
- Scala 2.13.15
- SBT 1.x
- PostgreSQL 12 or higher
- Docker (optional, for running PostgreSQL)

## Configuration

Application configuration is managed through `src/main/resources/application.conf`:

```hocon
database {
  driver = "org.postgresql.Driver"
  url = "jdbc:postgresql://localhost:5432/bardenas"
  user = "admin"
  password = "12345"
}

server {
  host = "0.0.0.0"
  port = 8080
}
```

## Getting Started

### 1. Setup Database

Create the PostgreSQL database:

```bash
createdb bardenas
```

Or using Docker:

```bash
docker run --name bardenas-postgres -e POSTGRES_PASSWORD=12345 -e POSTGRES_USER=admin -e POSTGRES_DB=bardenas -p 5432:5432 -d postgres:15
```

### 2. Run Database Migrations

```bash
sbt flywayMigrate
```

### 3. Run the Application

```bash
sbt run
```

The server will start on `http://localhost:8080`

## API Endpoints

### Products

- `POST /api/products/import` - Import products from CSV

## Testing

Run all tests:

```bash
sbt test
```

Run specific test suites:

```bash
# Integration tests
sbt "testOnly *IntegrationTest*"

# Behaviour tests
sbt "testOnly *BehaviourTest*"
```

## Database Migrations

Flyway is configured for database migrations. Migration files should be placed in `src/main/resources/db/migration/`.

Available Flyway commands:

```bash
sbt flywayMigrate   # Apply migrations
sbt flywayClean     # Clean database
sbt flywayInfo      # Migration status
```

## Project Structure

- **Use Cases**: Business logic orchestration (e.g., `ImportProductsUseCase`)
- **Controllers**: HTTP request handlers
- **Repositories**: Data persistence abstractions
- **Context**: Dependency injection containers for each bounded context

## Development

### Adding a New Feature

1. Define domain models in `domain/` package
2. Create repository contract in `domain/contract/`
3. Implement use case in `application/` package
4. Add repository implementation in `infrastructure/`
5. Create HTTP controller for the feature
6. Register routes in the context class
7. Wire up the context in `App.scala`

## Monitoring

The application includes Kamon for metrics and monitoring. Metrics can be exposed and collected according to your monitoring setup.

## License

[Add your license here]

## Contributors

[Add contributors here]
