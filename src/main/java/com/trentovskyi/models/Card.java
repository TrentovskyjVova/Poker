package com.trentovskyi.models;

public class Card {

    public static final char CLUBS = 'C';
    public static final char DIAMONDS = 'D';
    public static final char HEARTS = 'H';
    public static final char SPADES = 'S';

    private String card;
    private FaceValue faceValue;
    private char suit;

    public Card(String card) {
        this.card = card;
        this.faceValue = new FaceValue(card.charAt(0));
        this.suit = card.charAt(1);
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public FaceValue getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(FaceValue faceValue) {
        this.faceValue = faceValue;
    }

    public char getSuit() {
        return suit;
    }

    public void setSuit(char suit) {
        this.suit = suit;
    }
}
