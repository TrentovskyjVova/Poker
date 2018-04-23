package com.trentovskyi.processor;

import com.trentovskyi.models.Card;
import com.trentovskyi.models.Game;

import java.util.*;

import static com.trentovskyi.models.Card.STRAIGHT_SEQUENCE;
import static com.trentovskyi.processor.Combination.*;

public class PokerProcessor implements IProcessor {

    @Override
    public void process(List<Game> inputData) {
        for (Game game : inputData) {
            List<Character> flushPossibleSuits = checkFlushPossible(game);
            if (!flushPossibleSuits.isEmpty()) {
                if (findStraightFlush(game, flushPossibleSuits)) {
                    game.setBestHand(STRAIGHT_FLUSH);
                } else {
                    game.setBestHand(FLUSH);
                }
            } else {
                List<List<Character>> allFacesCombinations = findAllFacesCombinations(game);
                String bestHand = findBestHand(allFacesCombinations);
                game.setBestHand(bestHand);
            }
        }
    }

    private List<Character> checkFlushPossible(Game game) {
        SuitCounter countedSuits = new SuitCounter(COMBINATION_NEED_CARDS);
        countedSuits.putAll(game.getAllCards());

        if (!countedSuits.containsNeededSuitsCount()) {
            return Collections.emptyList();
        }

        return checkFlushSuit(game, countedSuits.getKeys());
    }

    private List<Character> checkFlushSuit(Game game, List<Character> suits) {
        List<Character> flushSuits = new ArrayList<>();

        for (char suit : suits) {
            int handSuitsCount = 0;
            for (Card card : game.getHandCards()) {
                if (card.getSuit() == suit) {
                    handSuitsCount++;
                }
            }

            boolean isSuitOk = true;
            for (int i = 0; i < COMBINATION_NEED_CARDS - handSuitsCount; i++) {
                if (game.getDeckCards()[i].getSuit() != suit) {
                    isSuitOk = false;
                    break;
                }
            }
            if (isSuitOk) {
                flushSuits.add(suit);
            }
        }
        return flushSuits;
    }

    private boolean findStraightFlush(Game game, List<Character> flushPossibleSuits) {
        List<List<Character>> combinations = prepareFlushCombinations(game, flushPossibleSuits);

        for (List<Character> combination : combinations) {
            if (Collections.indexOfSubList(STRAIGHT_SEQUENCE, combination) != -1) {
                return true;
            }
        }
        return false;
    }

    private List<List<Character>> prepareFlushCombinations(Game game, List<Character> flushSuits) {
        List<List<Character>> combinations = new ArrayList<>();

        for (Character suit : flushSuits) {
            List<Card> handCards = new ArrayList<>();
            for (Card card : game.getHandCards()) {
                if (card.getSuit() == suit) {
                    handCards.add(card);
                }
            }

            List<List<Card>> handCardsPawerset = powerset(handCards);

            for (List<Card> combination : handCardsPawerset) {
                int cardsFromDeck = COMBINATION_NEED_CARDS - combination.size();
                for (int i = 0; i < cardsFromDeck; i++) {
                    if (game.getDeckCards()[i].getSuit() == suit) {
                        combination.add(game.getDeckCards()[i]);
                    }
                }

                if (combination.size() == COMBINATION_NEED_CARDS) {
                    combinations.addAll(prepareCombination(combination));
                }
            }
        }
        return combinations;
    }

    private List<List<Character>> prepareCombination(List<Card> combination) {
        List<List<Character>> combinations = new ArrayList<>();
        List<Character> faces = new ArrayList<>();
        for (Card card : combination) {
            faces.add(card.getFaceValue());
        }
        Collections.sort(faces);
        combinations.add(faces);

        if (faces.get(0) == Card.ACE) {
            List<Character> aceReverse = new ArrayList<>(faces.subList(1, COMBINATION_NEED_CARDS));
            aceReverse.add(Card.ACE);
            combinations.add(aceReverse);
        }
        return combinations;
    }

    private <T> List<List<T>> powerset(Collection<T> list) {
        List<List<T>> ps = new ArrayList<>();
        ps.add(new ArrayList<>());

        for (T item : list) {
            List<List<T>> newPs = new ArrayList<>();

            for (List<T> subset : ps) {
                newPs.add(subset);

                List<T> newSubset = new ArrayList<>(subset);
                newSubset.add(item);
                newPs.add(newSubset);
            }

            ps = newPs;
        }
        Collections.reverse(ps);
        return ps;
    }

    private List<List<Character>> findAllFacesCombinations(Game game) {
        List<List<Character>> combinations = new ArrayList<>();

        List<List<Card>> handCardsPawerset = powerset(Arrays.asList(game.getHandCards()));
        for (List<Card> combination : handCardsPawerset) {
            int cardsFromDeck = COMBINATION_NEED_CARDS - combination.size();
            combination.addAll(Arrays.asList(game.getDeckCards()).subList(0, cardsFromDeck));

            combinations.addAll(prepareCombination(combination));
        }
        return combinations;
    }

    private String findBestHand(List<List<Character>> combinations) {
        String bestHand = HIGHEST_CARD;
        int handRank = RANK_HIGHEST_CARD;

        FaceCounter facesCount = new FaceCounter();
        for (List<Character> combination : combinations) {
            facesCount.clear();
            facesCount.putAll(combination);

            if (facesCount.getKeysCount() == DISTINCT_FACES_FOR_FOUR_OF_A_KIND
                    && facesCount.containsSameFaces(FOUR_OF_A_KIND_NEED_CARDS)) {
                return FOUR_OF_A_KIND;
            }

            if (handRank >= RANK_FULL_HOUSE) {
                continue;
            }
            if (facesCount.getKeysCount() == DISTINCT_FACES_FOR_FULL_HOUSE) {
                bestHand = FULL_HOUSE;
                handRank = RANK_FULL_HOUSE;
            }

            if (handRank >= RANK_STRAIGHT) {
                continue;
            }
            if (facesCount.getKeysCount() == COMBINATION_NEED_CARDS
                    && Collections.indexOfSubList(STRAIGHT_SEQUENCE, combination) != -1) {
                bestHand = STRAIGHT;
                handRank = RANK_STRAIGHT;
            }

            if (handRank >= RANK_THREE_OF_A_KIND) {
                continue;
            }
            if (facesCount.getKeysCount() == THREE_OF_A_KIND_NEED_CARDS
                    && facesCount.containsSameFaces(THREE_OF_A_KIND_NEED_CARDS)) {
                bestHand = THREE_OF_A_KIND;
                handRank = RANK_THREE_OF_A_KIND;
            }

            if (handRank >= RANK_TWO_PAIRS) {
                continue;
            }
            if (facesCount.getKeysCount() == DISTINCT_FACES_FOR_TWO_PAIRS) {
                bestHand = TWO_PAIRS;
                handRank = RANK_TWO_PAIRS;
            }

            if (handRank >= RANK_ONE_PAIR) {
                continue;
            }
            if (facesCount.getKeysCount() == DISTINCT_FACES_FOR_ONE_PAIR) {
                bestHand = ONE_PAIR;
                handRank = RANK_ONE_PAIR;
            }
        }
        return bestHand;
    }
}
