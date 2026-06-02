package com.accenture.accountmanagement.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;

public class BalanceUpdateRequest {
    @NotNull(message = "Amount is required.")
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
