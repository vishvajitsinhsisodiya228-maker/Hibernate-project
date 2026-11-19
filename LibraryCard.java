package com.library;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "library_cards")
public class LibraryCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    private Integer cardId;

    @Column(name = "card_number", unique = true, length = 10)
    private String cardNumber;

    @Column(name = "valid_till_year")
    private int validTillYear;

    public LibraryCard() {
    }

    public LibraryCard(String cardNumber, int validTillYear) {
        this.cardNumber = cardNumber;
        this.validTillYear = validTillYear;
    }

    public Integer getCardId() {
        return cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getValidTillYear() {
        return validTillYear;
    }
}
