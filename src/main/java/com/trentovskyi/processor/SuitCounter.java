package com.trentovskyi.processor;

import com.trentovskyi.models.Card;

import java.util.ArrayList;
import java.util.List;

public class SuitCounter {

    private int[] suits = new int[Card.suitsSetSize];
    private int minSuitsCount;

    public SuitCounter(int minSuitsCount) {
        this.minSuitsCount = minSuitsCount;
    }

    public boolean containsNeededSuitsCount() {
        for (int count : suits) {
            if (count >= minSuitsCount) {
                return true;
            }
        }
        return false;
    }

    public List<Character> getKeys() {
        List<Character> chars = new ArrayList<>();
        for (int i = 0; i < suits.length; i++) {
            if (suits[i] >= minSuitsCount) {
                chars.add(Card.SUIT_SEQUENCE.get(i));
            }
        }
        return chars;
    }

    public void putAll(Card[] combination) {
        for (Card card : combination) {
            int index = Card.SUIT_SEQUENCE.indexOf(card.getSuit());
            suits[index]++;
        }
    }
}
