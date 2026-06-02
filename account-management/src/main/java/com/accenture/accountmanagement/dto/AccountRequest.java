package com.accenture.accountmanagement.dto;

import java.math.BigDecimal;

import com.accenture.accountmanagement.enums.AccountStatus;
import com.accenture.accountmanagement.enums.AccountType;

import jakarta.validation.constraints.NotNull;

public class AccountRequest {
    @NotNull(message = "Account type is required.")
    private AccountType accountType;

    @NotNull
    private BigDecimal balance = BigDecimal.ZERO;

    @NotNull(message = "Account status is required")
    private AccountStatus accountStatus;

    @NotNull(message = "Currency is required")
    private String currency;

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
