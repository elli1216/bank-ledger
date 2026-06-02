# account-management

Spring Boot 4.0.6 / Java 26 / Maven microservice — part of the `bank-ledger` system.
Source of truth for customer profiles, accounts, and cards.

## Quick start

```bash
# Prerequisites: MySQL on localhost:3306 with db `bank-ledger`, Eureka on :8761
mvnw.cmd spring-boot:run          # Windows
./mvnw spring-boot:run            # Unix
mvnw.cmd test                     # context-load smoke tests only
```

Environment is loaded from `.env` (`SERVER_PORT=8082`, `DB_USERNAME=root`, `DB_PASSWORD=password`). Active profile is `dev` (set in `application.properties`).

## Architecture

- **Entrypoint:** `com.accenture.accountmanagement.AccountManagementApplication`
- **Layers:** controller → service → repository → model (JPA entities)
- **Package:** `com.accenture.accountmanagement`
- **DTOs** in `dto/` — entities are never exposed directly in REST responses

### Domain

Profile (1) ──→ Account (many) ──→ Card (many)

| Entity  | Table      | Key fields                                                                 |
|---------|------------|----------------------------------------------------------------------------|
| Profile | `profiles` | firstName, middleName?, lastName, email, phoneNumber, dateOfBirth          |
| Account | `accounts` | accountNumber, accountType (CHECKING/SAVINGS), balance, accountStatus (ACTIVE/DORMANT/FROZEN/CLOSED), currency |
| Card    | `cards`    | cardNumber (16 chars), cvv, expiryDate, cardType (DEBIT/CREDIT), cardStatus (ACTIVE/EXPIRED/BLOCKED), pin? |

Card has no `@PrePersist` timestamp — unlike Profile/Account which set `createdAt` via `@PrePersist`.

### Auto-generated values

- **Card number** — Luhn-valid 16 digits. DEBIT prefix `4`, CREDIT prefix `5`.
- **CVV** — 3 random digits.
- **Expiry date** — last day of month, 6 years from now.
- **Account number** — prefix `100` (CHECKING) or `200` (SAVINGS) + 7 random digits.

### REST API

All endpoints return DTOs, not entities.

| Endpoint                                   | Controller        | Notes                                  |
|--------------------------------------------|-------------------|----------------------------------------|
| `GET /api/profiles`                        | ProfileController |                                        |
| `GET /api/profiles/{id}`                   | ProfileController |                                        |
| `POST /api/profiles`                       | ProfileController | Body: `ProfileRequest`                 |
| `PUT /api/profiles/{id}`                   | ProfileController | Body: `ProfileRequest`                 |
| `DELETE /api/profiles/{id}`                | ProfileController |                                        |
| `GET /api/profiles/{id}/accounts`          | ProfileController | Accounts under this profile            |
| `GET /api/profiles/{id}/cards`             | ProfileController | Cards across all accounts of profile   |
| `POST /api/profiles/{id}/accounts`         | ProfileController | Create account under profile           |
| `GET /api/accounts`                        | AccountController |                                        |
| `GET /api/accounts/{id}`                   | AccountController |                                        |
| `PUT /api/accounts/{id}`                   | AccountController | Body: `AccountRequest`                 |
| `DELETE /api/accounts/{id}`                | AccountController |                                        |
| `GET /api/accounts/{id}/cards`             | AccountController | Cards for this account                 |
| `POST /api/accounts/{id}/cards`            | AccountController | Create card (body sends `cardType` + `cardStatus` only) |
| `POST /api/accounts/{id}/transactions`     | AccountController | Atomic balance update (`BalanceUpdateRequest`) |

Note: `POST /api/accounts` does not exist. Accounts are created via `POST /api/profiles/{id}/accounts`.

### Transaction endpoint

`POST /api/accounts/{id}/transactions` with `{"amount": 50.00}` (deposit) or `{"amount": -50.00}` (withdrawal).

Runs `UPDATE accounts SET balance = balance + :amount WHERE id = :id AND balance + :amount >= 0` — atomic, no race window. Throws `InsufficientBalanceException` → 422 if overdraft.

### Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`):
- `InsufficientBalanceException` → 422
- `RuntimeException` → 404 (not found)
- `MethodArgumentNotValidException` → 400 (field-level messages)
- `Exception` → 500 (generic message only)

## DTOs (in `dto/`)

| DTO                | Role                                  |
|--------------------|---------------------------------------|
| `ProfileRequest`   | Create/update profile                 |
| `ProfileResponse`  | Profile read (no accounts list)       |
| `AccountRequest`   | Create/update account                 |
| `AccountResponse`  | Account read (no profile/cards)       |
| `CardResponse`     | Card read (masked number, no CVV/PIN) |
| `BalanceUpdateRequest` | Single `amount` field             |

## Key config

- **JPA:** `ddl-auto=update` — schema auto-managed, no migration scripts
- **MySQL:** `jdbc:mysql://localhost:3306/bank-ledger`
- **Eureka:** `http://localhost:8761/eureka/`
- No static analysis, linting, or formatting tools configured