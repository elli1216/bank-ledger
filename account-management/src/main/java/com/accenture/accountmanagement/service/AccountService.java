package com.accenture.accountmanagement.service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.accenture.accountmanagement.dto.AccountRequest;
import com.accenture.accountmanagement.dto.AccountResponse;
import com.accenture.accountmanagement.dto.CardResponse;
import com.accenture.accountmanagement.enums.AccountType;
import com.accenture.accountmanagement.exception.InsufficientBalanceException;
import com.accenture.accountmanagement.model.Account;
import com.accenture.accountmanagement.model.Card;
import com.accenture.accountmanagement.repository.AccountRepository;
import com.accenture.accountmanagement.repository.CardRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountService {
    private final CardService cardService;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final SecureRandom random = new SecureRandom();

    public AccountService(AccountRepository accountRepository, CardRepository cardRepository,
            CardService cardService) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.cardService = cardService;
    }

    public List<AccountResponse> getAllAccounts() {
        logger.info("Fetching all accounts.");
        return accountRepository.findAll().stream()
                .map(AccountResponse::fromEntity)
                .toList();
    }

    public List<AccountResponse> getAccountsByProfileId(Long profileId) {
        logger.info("Fetching accounts for profile id: {}", profileId);
        return accountRepository.findByProfileId(profileId).stream()
                .map(AccountResponse::fromEntity)
                .toList();
    }

    public AccountResponse getAccountById(Long id) {
        return AccountResponse.fromEntity(findEntityById(id));
    }

    private Account findEntityById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> {
            logger.warn("Account Id not found: {}", id);
            return new RuntimeException("Account not found: " + id);
        });
    }

    public AccountResponse createAccount(AccountRequest request) {
        Account account = new Account();
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setAccountStatus(request.getAccountStatus());
        account.setCurrency(request.getCurrency());
        return createAccount(account);
    }

    public AccountResponse createAccount(Account account) {
        account.setAccountNumber(generateUniqueAccountNumber(account.getAccountType()));
        Account saved = accountRepository.save(account);
        return AccountResponse.fromEntity(saved);
    }

    public AccountResponse updateAccount(Long id, AccountRequest request) {
        Account existing = findEntityById(id);
        existing.setAccountStatus(request.getAccountStatus());
        existing.setAccountType(request.getAccountType());
        existing.setBalance(request.getBalance());
        existing.setCurrency(request.getCurrency());
        Account saved = accountRepository.save(existing);
        return AccountResponse.fromEntity(saved);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public String generateUniqueAccountNumber(AccountType accountType) {
        String prefix = (accountType == AccountType.CHECKING) ? "100" : "200";
        String accountNumber;
        boolean exists;

        do {
            int randomCore = 1000000 + random.nextInt(9000000); // Generates a random integer from 1000000 to 9999999
            accountNumber = prefix + randomCore;
            exists = accountRepository.existsByAccountNumber(accountNumber);
        } while (exists);

        return accountNumber;
    }

    public Card createCardForAccount(Long id, Card card) {
        Account account = accountRepository.findById(id).orElseThrow(() -> {
            logger.warn("Account id not found: {}", id);
            return new RuntimeException("Account id not found: " + id);
        });
        card.setAccount(account);
        return cardService.createCard(card);
    }

    public List<CardResponse> getCardsByAccountId(Long accountId) {
        findEntityById(accountId);
        return cardRepository.findByAccountId(accountId).stream()
                .map(CardResponse::fromEntity)
                .toList();
    }

    @Transactional
    public AccountResponse applyTransaction(Long id, BigDecimal amount) {
        findEntityById(id);
        int updated = accountRepository.updateBalance(id, amount);
        if (updated == 0) {
            throw new InsufficientBalanceException("Insufficient balance for account " + id);
        }
        return getAccountById(id);
    }
}
