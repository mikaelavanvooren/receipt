# Grocery Price Tracker

A Spring Boot REST API for tracking and comparing grocery prices across different stores. Built to solve a real problem I have - I shop at multiple stores and wanted an easy way to see where items are cheapest.

## Why I Built This

I wanted hands-on experience with Spring Boot, PostgreSQL, and Docker, and I wanted a way to track grocery prices to find the best deals. This project gave me the opportunity to build a production-ready backend from scratch.

## Tech Stack

- **Backend**: Spring Boot 3.x, Java 21
- **Database**: PostgreSQL with Flyway migrations
- **Testing**: JUnit 5, Mockito, H2
- **Containerization**: Docker Compose
- **Architecture**: Layered (Controller → Service → Repository)

## Features

- REST API for managing stores, products, and prices
- Price comparison endpoint to find cheapest store for any product
- Basic receipt parsing (regex-based, works for simple formats)
- Input validation with custom error handling
- Database migrations for schema version control
- Comprehensive test coverage (unit + integration tests)

## Quick Start
```bash
# Clone and start
git clone <repo-url>
cd receiptReader
docker-compose up --build

# API runs on http://localhost:8080
```

## Example API Usage

**Find cheapest price for a product:**
```bash
GET /api/prices/cheapest/1
```

**Compare prices across stores:**
```bash
GET /api/prices/compare/1
```

**Parse a receipt:**
```bash
POST /api/receipts/parse
Content-Type: text/plain

WALMART
Milk    $3.99
Bread   $2.49
```

## What I Learned

- **Spring Data JPA**: Relationships, custom queries, repository pattern
- **Database Design**: Foreign keys, normalization, migrations with Flyway
- **Testing Strategies**: When to use unit tests (mocking) vs integration tests (real DB)
- **Docker**: Multi-container orchestration, networking between services
- **API Design**: DTOs, error handling, validation

## Challenges & Solutions

- **Circular references**: Initially returned full nested objects in API responses, causing JSON serialization issues. Fixed by implementing DTOs.
- **Test isolation**: Learned the importance of `@BeforeEach` to reset database state between tests.
- **Docker networking**: Had to understand why containers use service names (e.g., `postgres:5432`) instead of `localhost`.

## Next Steps

The current implementation works well for manual entry, but the real value would come from automation:

- **AI-powered receipt parsing**: Use OCR + LLMs to extract data from receipt photos
- **Automated price tracking**: Scrape store websites for current prices
- **Price trends**: Show historical price changes and predict future trends
- **Web dashboard**: Visualize price comparisons and savings

## Running Tests
```bash
./gradlew test
```

Includes 12+ tests covering service logic and API endpoints.

---

**Author**: Mikaela Van Vooren
**GitHub**: [[Mikaela Van Vooren](https://github.com/mikaelavanvooren)]