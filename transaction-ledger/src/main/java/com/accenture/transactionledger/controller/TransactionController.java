package com.accenture.transactionledger.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.transactionledger.dto.DepositRequest;
import com.accenture.transactionledger.dto.TransactionResponse;
import com.accenture.transactionledger.dto.TransferRequest;
import com.accenture.transactionledger.dto.WithdrawRequest;
import com.accenture.transactionledger.service.TransactionService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @GetMapping("/account/{accountId}")
    public List<TransactionResponse> getTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse result = transactionService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        request.setDescription("Amount of " + request.getAmount() + " have been deposited.");
        TransactionResponse result = transactionService.deposit(
                request.getCardNumber(), request.getAmount(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        request.setDescription("Amount of " + request.getAmount() + " have been withdrawn.");
        TransactionResponse result = transactionService.withdraw(
                request.getCardNumber(), request.getAmount(), request.getDescription());
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
