package com.accenture.accountmanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.accountmanagement.dto.AccountRequest;
import com.accenture.accountmanagement.dto.AccountResponse;
import com.accenture.accountmanagement.dto.BalanceUpdateRequest;
import com.accenture.accountmanagement.dto.CardResponse;
import com.accenture.accountmanagement.model.Account;
import com.accenture.accountmanagement.model.Card;
import com.accenture.accountmanagement.service.AccountService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PutMapping("/{id}")
    public AccountResponse updateAccount(@PathVariable Long id, @RequestBody AccountRequest updatedAccount) {
        return accountService.updateAccount(id, updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cards")
    public List<CardResponse> getCardsByAccountId(@PathVariable Long id) {
        return accountService.getCardsByAccountId(id);
    }

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> createCardForAccount(@PathVariable Long id, @Valid @RequestBody Card card) {
        Card saved = accountService.createCardForAccount(id, card);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/{id}/transactions")
    public ResponseEntity<Account> applyTransaction(@PathVariable Long id,
            @Valid @RequestBody BalanceUpdateRequest request) {
        Account account = accountService.applyTransaction(id, request.getAmount());
        return ResponseEntity.ok(account);
    }

}
