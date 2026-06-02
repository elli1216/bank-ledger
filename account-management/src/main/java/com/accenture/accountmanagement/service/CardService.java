package com.accenture.accountmanagement.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.accenture.accountmanagement.dto.CardResponse;
import com.accenture.accountmanagement.enums.CardType;
import com.accenture.accountmanagement.model.Card;
import com.accenture.accountmanagement.repository.CardRepository;

@Service
public class CardService {
    private final Logger logger = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;
    private final SecureRandom random = new SecureRandom();

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<CardResponse> getAllCards() {
        logger.info("Fetching all cards.");
        return cardRepository.findAll()
                .stream()
                .map(CardResponse::fromEntity)
                .toList();
    }

    public Card getCardById(Long id) {
        logger.info("Fetching card by id: {}", id);
        return cardRepository.findById(id).orElseThrow(() -> {
            logger.warn("Card id not found: {}", id);
            return new RuntimeException("Card id not found: " + id);
        });
    }

    public Card createCard(Card card) {
        logger.info("Creating card: {}", card);
        card.setCardNumber(generateValidCardNumber(card.getCardType()));
        card.setCvv(generateCvv());
        card.setExpiryDate(generateExpiryDate());
        return cardRepository.save(card);
    }

    private String generateCvv() {
        return String.format("%03d", random.nextInt(1000));
    }

    private LocalDate generateExpiryDate() {
        return YearMonth.now().plusYears(6).atEndOfMonth();
    }

    public Card updateCard(Long id, Card updatedCard) {
        Card existing = getCardById(id);
        logger.info("Updating card: {}", id);
        existing.setCardStatus(updatedCard.getCardStatus());
        existing.setCardType(updatedCard.getCardType());
        Card saved = cardRepository.save(existing);
        return saved;
    }

    public void deleteCard(Long id) {
        logger.info("Delete card: {}", id);
        cardRepository.deleteById(id);
    }

    public CardResponse getCardByCardNumber(String cardNumber) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> {
                    logger.warn("Card number not found: {}", cardNumber);
                    return new RuntimeException("Card not found: " + cardNumber);
                });

        return CardResponse.fromEntity(card);
    }

    public String generateValidCardNumber(CardType cardType) {
        String prefix = switch (cardType) {
            case DEBIT -> "4";
            case CREDIT -> "5";
        };
        String number;
        boolean exists;
        do {
            StringBuilder sb = new StringBuilder(prefix);
            for (int i = 0; i < 14; i++) {
                sb.append(random.nextInt(10));
            }
            int checkDigit = computeLuhnCheckDigit(sb.toString());
            sb.append(checkDigit);
            number = sb.toString();
            exists = cardRepository.existsByCardNumber(number);
        } while (exists);
        return number;
    }

    private int computeLuhnCheckDigit(String digits) {
        int sum = 0;
        boolean alternate = true;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = digits.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum * 9) % 10;
    }
}
