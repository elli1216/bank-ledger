# account-management

Spring Boot 4.0.6 / Java 26 / Maven microservice — part of the `bank-ledger` system.

## Quick start

```bash
# Prerequisites: MySQL on localhost:3306 with db `bank-ledger`, Eureka on :8761
mvnw.cmd spring-boot:run          # Windows
./mvnw spring-boot:run            # Unix
mvnw.cmd test                     # run tests
```

Environment is loaded from `.env` at the project root (`SERVER_PORT=8082`, `DB_USERNAME=root`, `DB_PASSWORD=password`). The active profile is `dev` (set in `application.properties`).

## Architecture

- **Entrypoint:** `com.accenture.accountmanagement.AccountManagementApplication` (`src/main/java/...`)
- **Layers:** controller → service → repository → model (JPA entities)
- **Package:** `com.accenture.accountmanagement`

### Domain

Profile (1) ──→ Account (many) ──→ Card (many)

| Entity  | Table      | Key fields                                                                 |
|---------|------------|----------------------------------------------------------------------------|
| Profile | `profiles` | firstName, middleName?, lastName, email, phoneNumber, dateOfBirth          |
| Account | `accounts` | accountNumber, customerId, accountType (CHECKING/SAVINGS), balance, accountStatus (ACTIVE/DORMANT/FROZEN/CLOSED), currency |
| Card    | `cards`    | cardNumber (16 chars), cvv, expiryDate, cardType (DEBIT/CREDIT), cardStatus (ACTIVE/EXPIRED/BLOCKED), pin? |

Card has no `@PrePersist` timestamp — unlike Profile/Account which set `createdAt` via `@PrePersist`.

### REST API

| Endpoint             | Controller         | Status       |
|----------------------|--------------------|--------------|
| `GET /api/accounts`  | `AccountController`| Implemented  |
| `GET /api/accounts/{id}` | AccountController | Implemented  |
| `POST /api/accounts` | AccountController  | Implemented  |
| `GET/POST /api/profiles` | ProfileController | **Stub** — no endpoints yet |

### Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`):
- `RuntimeException` → 404
- `MethodArgumentNotValidException` → 400 (field-level messages)
- `Exception` → 500 (generic message only)

## Key config

- **JPA:** `ddl-auto=update` — schema auto-managed, no migration scripts
- **MySQL:** `jdbc:mysql://localhost:3306/bank-ledger`
- **Eureka:** `http://localhost:8761/eureka/`
- **ProfileService** is a stub (empty class)
- No static analysis, linting, or formatting tools configured
