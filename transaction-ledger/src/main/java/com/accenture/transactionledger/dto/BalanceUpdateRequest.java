package com.accenture.transactionledger.dto;

import java.math.BigDecimal;

public class BalanceUpdateRequest {
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
