package com.accenture.transactionledger.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.accenture.transactionledger.dto.AccountResponse;
import com.accenture.transactionledger.dto.BalanceUpdateRequest;
import com.accenture.transactionledger.dto.CardResponse;

@FeignClient(name = "account-management")
public interface AccountServiceClient {
    @GetMapping("/api/accounts/{id}")
    AccountResponse getAccountById(@PathVariable("id") Long id);

    @PostMapping("/api/accounts/{id}/transactions")
    AccountResponse applyTransaction(@PathVariable("id") Long id, @RequestBody BalanceUpdateRequest request);

    @GetMapping("/api/cards/by-number/{cardNumber}")
    CardResponse getCardByNumber(@PathVariable("cardNumber") String cardNumber);
}
