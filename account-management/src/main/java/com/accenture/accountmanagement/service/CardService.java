package com.accenture.accountmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.accenture.accountmanagement.model.Card;
import com.accenture.accountmanagement.repository.CardRepository;

@Service
public class CardService {
    private final Logger logger = LoggerFactory.getLogger(CardService.class);
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        logger.info("Fetching all cards.");
        return cardRepository.findAll();
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
        return cardRepository.save(card);
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
}
