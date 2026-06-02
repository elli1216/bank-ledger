package com.accenture.transactionledger.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.accenture.transactionledger.dto.BalanceUpdateRequest;
import com.accenture.transactionledger.dto.TransactionResponse;
import com.accenture.transactionledger.dto.TransferRequest;
import com.accenture.transactionledger.enums.TransactionStatus;
import com.accenture.transactionledger.enums.TransactionType;
import com.accenture.transactionledger.exception.InsufficientBalanceException;
import com.accenture.transactionledger.model.TransactionLog;
import com.accenture.transactionledger.repository.TransactionLogRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {
    private final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionLogRepository transactionLogRepository;
    private final AccountServiceClient accountServiceClient;
    private final SecureRandom random = new SecureRandom();

    public TransactionService(TransactionLogRepository transactionLogRepository,
            AccountServiceClient accountServiceClient) {
        this.accountServiceClient = accountServiceClient;
        this.transactionLogRepository = transactionLogRepository;
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionLogRepository.findAll().stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    public TransactionResponse getTransactionById(Long id) {
        return TransactionResponse.fromEntity(findEntityById(id));
    }

    public List<TransactionResponse> getTransactionsByAccountId(Long id) {
        return transactionLogRepository
                .findByFromAccountIdOrToAccountIdOrderByCreatedAtDesc(id, id)
                .stream()
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        String reference = generateTransactionReference();

        logger.info("Initiating transfer {}: {} -> {}, amount: {}",
                reference, request.getFromAccountId(), request.getToAccountId(), request.getAmount());

        TransactionLog log = new TransactionLog();
        log.setTransactionReference(reference);
        log.setFromAccountId(request.getFromAccountId());
        log.setToAccountId(request.getToAccountId());
        log.setAmount(request.getAmount());
        log.setTransactionType(TransactionType.TRANSFER);
        log.setDescription(request.getDescription());

        try {
            accountServiceClient.applyTransaction(request.getFromAccountId(),
                    new BalanceUpdateRequest(request.getAmount().negate()));
            accountServiceClient.applyTransaction(request.getToAccountId(),
                    new BalanceUpdateRequest(request.getAmount()));
            log.setStatus(TransactionStatus.COMPLETED);
            logger.info("Transfer {} completed successfully", reference);
        } catch (Exception e) {
            log.setStatus(TransactionStatus.FAILED);
            transactionLogRepository.save(log);
            logger.error("Transfer {} failed: {}", reference, e.getMessage());
            if (e instanceof InsufficientBalanceException) {
                throw e;
            }
            throw new RuntimeException("Transfer failed: " + e.getMessage());
        }

        TransactionLog saved = transactionLogRepository.save(log);
        return TransactionResponse.fromEntity(saved);
    }

    private TransactionLog findEntityById(Long id) {
        return transactionLogRepository.findById(id).orElseThrow(() -> {
            logger.warn("Transaction not found: {}", id);
            return new RuntimeException("Transaction not found: " + id);
        });
    }

    @Transactional
    public TransactionResponse deposit(Long accountId, BigDecimal amount, String description) {
        String reference = generateTransactionReference();
        logger.info("Initiating deposit {}: account {} amount: {}", reference, accountId, amount);

        TransactionLog log = new TransactionLog();
        log.setTransactionReference(reference);
        log.setToAccountId(accountId);
        log.setAmount(amount);
        log.setTransactionType(TransactionType.DEPOSIT);
        log.setDescription(description);

        try {
            accountServiceClient.applyTransaction(accountId, new BalanceUpdateRequest(amount));
            log.setStatus(TransactionStatus.COMPLETED);
        } catch (Exception e) {
            log.setStatus(TransactionStatus.FAILED);
            transactionLogRepository.save(log);
            throw new RuntimeException("Deposit failed: " + e.getMessage());
        }

        TransactionLog saved = transactionLogRepository.save(log);
        return TransactionResponse.fromEntity(saved);
    }

    @Transactional
    public TransactionResponse withdraw(Long accountId, BigDecimal amount, String description) {
        String reference = generateTransactionReference();
        logger.info("Initiating withdrawal {}: account {} amount: {}", reference, accountId, amount);

        TransactionLog log = new TransactionLog();
        log.setTransactionReference(reference);
        log.setFromAccountId(accountId);
        log.setToAccountId(accountId);
        log.setAmount(amount);
        log.setTransactionType(TransactionType.WITHDRAWAL);
        log.setDescription(description);

        try {
            accountServiceClient.applyTransaction(accountId, new BalanceUpdateRequest(amount.negate()));
            log.setStatus(TransactionStatus.COMPLETED);
        } catch (Exception e) {
            log.setStatus(TransactionStatus.FAILED);
            transactionLogRepository.save(log);
            if (e instanceof InsufficientBalanceException) {
                throw e;
            }
            throw new RuntimeException("Withdrawal failed: " + e.getMessage());
        }

        TransactionLog saved = transactionLogRepository.save(log);
        return TransactionResponse.fromEntity(saved);
    }

    private String generateTransactionReference() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("TXN");
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
