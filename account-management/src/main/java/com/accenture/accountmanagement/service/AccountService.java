package com.accenture.accountmanagement.service;

import java.security.SecureRandom;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.accenture.accountmanagement.enums.AccountType;
import com.accenture.accountmanagement.model.Account;
import com.accenture.accountmanagement.model.Card;
import com.accenture.accountmanagement.repository.AccountRepository;

@Service
public class AccountService {
    private final CardService cardService;
    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final SecureRandom random = new SecureRandom();

    public AccountService(AccountRepository accountRepository, CardService cardService) {
        this.accountRepository = accountRepository;
        this.cardService = cardService;
    }

    public List<Account> getAllAccounts() {
        logger.info("Fetching all accounts.");
        return accountRepository.findAll();
    }

    public List<Account> getAccountsByProfileId(Long profileId) {
        logger.info("Fetching accounts for profile id: {}", profileId);
        return accountRepository.findByProfileId(profileId);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Account Id not found: {}", id);
                    return new RuntimeException("Account not found: " + id);
                });
    }

    public Account createAccount(Account account) {
        account.setAccountNumber(generateUniqueAccountNumber(account.getAccountType()));
        Account saved = accountRepository.save(account);
        return saved;
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        Account existing = getAccountById(id);
        existing.setAccountStatus(updatedAccount.getAccountStatus());
        existing.setAccountType(updatedAccount.getAccountType());
        existing.setBalance(updatedAccount.getBalance());
        existing.setCurrency(updatedAccount.getCurrency());
        existing.setProfile(updatedAccount.getProfile());
        Account saved = accountRepository.save(existing);
        return saved;
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
}
