package com.trentovskyi.models;

import java.util.Arrays;
import java.util.List;

public final class Card {

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

    private static final char CLUBS = 'C';
    private static final char DIAMONDS = 'D';
    private static final char HEARTS = 'H';
    private static final char SPADES = 'S';

    public static final List<Character> STRAIGHT_SEQUENCE = Arrays.asList(
            ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    );

    public static final List<Character> SUIT_SEQUENCE = Arrays.asList(
            CLUBS, DIAMONDS, HEARTS, SPADES
    );

    public static final int facesSetSize = 13;
    public static final int suitsSetSize = 4;

    private static final Card[] DECK = {
            new Card(ACE, CLUBS, ORIGIN_ACE),
            new Card(TWO, CLUBS),
            new Card(THREE, CLUBS),
            new Card(FOUR, CLUBS),
            new Card(FIVE, CLUBS),
            new Card(SIX, CLUBS),
            new Card(SEVEN, CLUBS),
            new Card(EIGHT, CLUBS),
            new Card(NINE, CLUBS),
            new Card(TEN, CLUBS, ORIGIN_TEN),
            new Card(JACK, CLUBS),
            new Card(QUEEN, CLUBS),
            new Card(KING, CLUBS, ORIGIN_KING),

            new Card(ACE, DIAMONDS, ORIGIN_ACE),
            new Card(TWO, DIAMONDS),
            new Card(THREE, DIAMONDS),
            new Card(FOUR, DIAMONDS),
            new Card(FIVE, DIAMONDS),
            new Card(SIX, DIAMONDS),
            new Card(SEVEN, DIAMONDS),
            new Card(EIGHT, DIAMONDS),
            new Card(NINE, DIAMONDS),
            new Card(TEN, DIAMONDS, ORIGIN_TEN),
            new Card(JACK, DIAMONDS),
            new Card(QUEEN, DIAMONDS),
            new Card(KING, DIAMONDS, ORIGIN_KING),

            new Card(ACE, HEARTS, ORIGIN_ACE),
            new Card(TWO, HEARTS),
            new Card(THREE, HEARTS),
            new Card(FOUR, HEARTS),
            new Card(FIVE, HEARTS),
            new Card(SIX, HEARTS),
            new Card(SEVEN, HEARTS),
            new Card(EIGHT, HEARTS),
            new Card(NINE, HEARTS),
            new Card(TEN, HEARTS, ORIGIN_TEN),
            new Card(JACK, HEARTS),
            new Card(QUEEN, HEARTS),
            new Card(KING, HEARTS, ORIGIN_KING),

            new Card(ACE, SPADES, ORIGIN_ACE),
            new Card(TWO, SPADES),
            new Card(THREE, SPADES),
            new Card(FOUR, SPADES),
            new Card(FIVE, SPADES),
            new Card(SIX, SPADES),
            new Card(SEVEN, SPADES),
            new Card(EIGHT, SPADES),
            new Card(NINE, SPADES),
            new Card(TEN, SPADES, ORIGIN_TEN),
            new Card(JACK, SPADES),
            new Card(QUEEN, SPADES),
            new Card(KING, SPADES, ORIGIN_KING),
    };

    private final char originFaceValue;
    private final char faceValue;
    private final char suit;

    private Card(char faceValue, char suit) {
        this.originFaceValue = faceValue;
        this.faceValue = faceValue;
        this.suit = suit;
    }

    private Card(char faceValue, char suit, char originFaceValue) {
        this.originFaceValue = originFaceValue;
        this.faceValue = faceValue;
        this.suit = suit;
    }

    public static Card getInstance(char faceValue, char suit) {
        int index = getIndex(faceValue, suit);
        return DECK[index];
    }

    public char getOriginFaceValue() {
        return originFaceValue;
    }

    public char getFaceValue() {
        return faceValue;
    }

    public char getSuit() {
        return suit;
    }

    private static int getIndex(char faceValue, char suit) {
        return getFaceIndex(faceValue) + getSuitOffset(suit);
    }

    public static int getFaceIndex(char faceValue) {
        int index = 0;
        switch (faceValue) {
            case ORIGIN_ACE:
            case ACE:
                index = 0;
                break;
            case TWO:
                index = 1;
                break;
            case THREE:
                index = 2;
                break;
            case FOUR:
                index = 3;
                break;
            case FIVE:
                index = 4;
                break;
            case SIX:
                index = 5;
                break;
            case SEVEN:
                index = 6;
                break;
            case EIGHT:
                index = 7;
                break;
            case NINE:
                index = 8;
                break;
            case ORIGIN_TEN:
            case TEN:
                index = 9;
                break;
            case JACK:
                index = 10;
                break;
            case QUEEN:
                index = 11;
                break;
            case ORIGIN_KING:
            case KING:
                index = 12;
                break;
        }
        return index;
    }

    public static int getSuitOffset(char suit) {
        int offset = 0;
        switch (suit) {
            case CLUBS:
                offset = 0;
                break;
            case DIAMONDS:
                offset = 13;
                break;
            case HEARTS:
                offset = 26;
                break;
            case SPADES:
                offset = 39;
                break;
        }
        return offset;
    }

}
