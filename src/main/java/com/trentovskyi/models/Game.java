package com.trentovskyi.models;

import java.util.Arrays;
import java.util.function.Consumer;

public class Game {

    private static final int MAX_HAND_DECK_CARDS_COUNT = 5;

    private Card[] allCards;
    private Card[] handCards = new Card[MAX_HAND_DECK_CARDS_COUNT];
    private Card[] deckCards = new Card[MAX_HAND_DECK_CARDS_COUNT];
    private String bestHand;

    public Game(Card[] allCards) {
        this.allCards = allCards;

        for (int i = 0; i < MAX_HAND_DECK_CARDS_COUNT; i++) {
            this.handCards[i] = (allCards[i]);
            this.deckCards[i] = allCards[i + MAX_HAND_DECK_CARDS_COUNT];
        }
    }

    public Card[] getHandCards() {
        return handCards;
    }

    public Card[] getAllCards() {
        return allCards;
    }

    public void setAllCards(Card[] allCards) {
        this.allCards = allCards;
    }

    public void setHandCards(Card[] handCards) {
        this.handCards = handCards;
    }

    public Card[] getDeckCards() {
        return deckCards;
    }

    public void setDeckCards(Card[] deckCards) {
        this.deckCards = deckCards;
    }

    public String getBestHand() {
        return bestHand;
    }

    public void setBestHand(String bestHand) {
        this.bestHand = bestHand;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Consumer<Card> consumer = c -> builder.append(c.getCard()).append(" ");
        builder.append("Hand: ");
        Arrays.stream(handCards).forEach(consumer);
        builder.append("Deck: ");
        Arrays.stream(deckCards).forEach(consumer);
        builder.append("Best hand: ");
        builder.append(bestHand);
        return builder.toString();
    }
}
