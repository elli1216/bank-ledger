package com.accenture.accountmanagement.model;

import java.time.LocalDate;

import com.accenture.accountmanagement.enums.CardStatus;
import com.accenture.accountmanagement.enums.CardType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    @NotNull
    @Size(min = 16, max = 16, message = "Card number length is invalid, must be 16 numbers.")
    private String cardNumber;

    @Column(nullable = false)
    @NotNull(message = "CVV is required.")
    private String cvv;

    @Column(nullable = false)
    @NotNull(message = "Expiry is required.")
    private LocalDate expiryDate;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Card type is required.")
    private CardType cardType;

    @Column(nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Card status is required.")
    private CardStatus cardStatus;

    @Column(nullable = true)
    private String pin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Card() {
    }

    public Card(Long id, String cardNumber, String cvv, LocalDate expiryDate, CardType cardType,
            CardStatus cardStatus, String pin) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.pin = pin;
    }

    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
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

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
