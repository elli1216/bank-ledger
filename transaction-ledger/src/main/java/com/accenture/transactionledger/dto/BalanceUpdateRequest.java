package com.accenture.transactionledger.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class BalanceUpdateRequest {
    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    public BalanceUpdateRequest() {
    }

    public BalanceUpdateRequest(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
