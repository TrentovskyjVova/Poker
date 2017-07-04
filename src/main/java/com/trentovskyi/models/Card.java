package com.trentovskyi.models;

import java.util.*;

public class Card {

    public static final char ACE = '1';
    private static final char ORIGIN_ACE = 'A';
    private static final char TWO = '2';
    private static final char THREE = '3';
    private static final char FOUR = '4';
    private static final char FIVE = '5';
    private static final char SIX = '6';
    private static final char SEVEN = '7';
    private static final char EIGHT = '8';
    private static final char NINE = '9';
    private static final char TEN = 'B';
    private static final char ORIGIN_TEN = 'T';
    private static final char JACK = 'J';
    private static final char QUEEN = 'Q';
    private static final char KING = 'R';
    private static final char ORIGIN_KING = 'K';

    public static final List<Character> STRAIGHT_SEQUENCE = Arrays.asList(
            ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    );

    private static final Map<String, Card> DECK = new HashMap<>();

    private String card;

    private char faceValue;
    private char suit;

    private Card(String card) {
        this.card = card;
        this.faceValue = replace(card.charAt(0));
        this.suit = card.charAt(1);
    }

    public static Card getInstance(String cardRepresentation) {
        return DECK.computeIfAbsent(cardRepresentation, Card::new);
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public char getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(char faceValue) {
        this.faceValue = faceValue;
    }

    public char getSuit() {
        return suit;
    }

    public void setSuit(char suit) {
        this.suit = suit;
    }

    private char replace(char origin) {
        if (origin == ORIGIN_ACE) return ACE;
        if (origin == ORIGIN_TEN) return TEN;
        if (origin == ORIGIN_KING) return KING;
        return origin;
    }
}
