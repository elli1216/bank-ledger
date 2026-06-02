package com.accenture.transactionledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.transactionledger.model.TransactionLog;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    List<TransactionLog> findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(Long fromAccountId, Long toAccountId);

    List<TransactionLog> findByToAccountIdOrderByCreatedAtDesc(Long toAccountId);

}
