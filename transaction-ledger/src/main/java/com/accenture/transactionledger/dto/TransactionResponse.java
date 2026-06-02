package com.accenture.transactionledger.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.accenture.transactionledger.enums.TransactionStatus;
import com.accenture.transactionledger.enums.TransactionType;
import com.accenture.transactionledger.model.TransactionLog;

public class TransactionResponse {
    private Long id;
    private String transactionReference;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private LocalDateTime createdAt;

    public static TransactionResponse fromEntity(TransactionLog log) {
        TransactionResponse dto = new TransactionResponse();
        dto.setId(log.getId());
        dto.setTransactionReference(log.getTransactionReference());
        dto.setFromAccountId(log.getFromAccountId());
        dto.setToAccountId(log.getToAccountId());
        dto.setAmount(log.getAmount());
        dto.setTransactionType(log.getTransactionType());
        dto.setStatus(log.getStatus());
        dto.setDescription(log.getDescription());
        dto.setCreatedAt(log.getCreatedAt());
        return dto;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(Long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
