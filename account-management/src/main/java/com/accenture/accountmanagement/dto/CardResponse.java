package com.accenture.accountmanagement.dto;

import java.time.LocalDate;

import com.accenture.accountmanagement.enums.CardStatus;
import com.accenture.accountmanagement.enums.CardType;
import com.accenture.accountmanagement.model.Card;

public class CardResponse {

    private Long id;
    private String cardNumber;
    private CardType cardType;
    private CardStatus cardStatus;
    private LocalDate expiryDate;
    private Long accountId;

    public static CardResponse fromEntity(Card card) {
        CardResponse dto = new CardResponse();
        dto.setId(card.getId());
        dto.setCardNumber(card.getCardNumber());
        dto.setCardType(card.getCardType());
        dto.setCardStatus(card.getCardStatus());
        dto.setExpiryDate(card.getExpiryDate());
        dto.setAccountId(card.getAccount().getId());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
