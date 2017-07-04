package com.trentovskyi.models;

import java.util.Arrays;
import java.util.List;

public class FaceValue {
    public static final char ACE = 'A';
    private static final char TWO = '2';
    private static final char THREE = '3';
    private static final char FOUR = '4';
    private static final char FIVE = '5';
    private static final char SIX = '6';
    private static final char SEVEN = '7';
    private static final char EIGHT = '8';
    private static final char NINE = '9';
    private static final char TEN = 'T';
    private static final char JACK = 'J';
    private static final char QUEEN = 'Q';
    private static final char KING = 'K';

    public static final List<Character> STRAIGHT_SEQUENCE = Arrays.asList(
            ACE, KING, QUEEN, JACK, TEN, NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO, ACE
    );

    private char face;
    private int value;

    public FaceValue(char face) {
        this.face = face;
        this.value = STRAIGHT_SEQUENCE.indexOf(face);
    }

    public char getFace() {
        return face;
    }

    public void setFace(char face) {
        this.face = face;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
