# account-management — system context

This is **one microservice** in the `bank-ledger` system. The other two services live in separate repos/artifacts and are **not** in this workspace.

## Peer services

| Service | Role | Depends on this service? |
|---------|------|--------------------------|
| **transaction-ledger** | Double-entry engine — real-time debits/credits, immutable audit logs, overdraft protection | Reads `accounts.balance` to reject transactions below zero |
| **reconciliation-batch** | Nightly balance verification, monthly interest distribution, statement compilation | Reads `accounts.balance` to verify against transaction sums |

## What this service owns (source of truth)

- **Customer profiles** — identity data (name, email, phone, DOB)
- **Accounts** — account number, type (CHECKING/SAVINGS), balance, status (ACTIVE/DORMANT/FROZEN/CLOSED), currency
- **Cards** — card number, CVV, expiry, type (DEBIT/CREDIT), status, PIN

## Key contracts with other services

- **Balance is the source of truth here.** `transaction-ledger` reads balance from this service before authorising a transaction, and writes back the updated balance after posting.
- **Status controls** (FREEZE/ACTIVATE) are managed here. `transaction-ledger` should check `accountStatus` before processing.
- **Account numbers** are generated here (prefix: `100` = CHECKING, `200` = SAVINGS + 7 random digits). Unique constraint enforced at the DB level.
- **No HTTP APIs yet for external service calls** — inter-service communication approach (REST/gRPC/messaging) is TBD.

## What this service does NOT do

- No transaction posting or ledger entries
- No overdraft logic
- No batch jobs or scheduled tasks
- No statement generation
