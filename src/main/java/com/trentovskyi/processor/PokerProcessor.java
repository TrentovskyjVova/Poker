package com.trentovskyi.processor;

import com.trentovskyi.models.Card;
import com.trentovskyi.models.Game;

import java.util.*;
import java.util.stream.Collectors;


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
        Map<Character, Integer> countedSuits = new HashMap<>();
        for (Card card : game.getAllCards()) {
            Integer count = countedSuits.get(card.getSuit());
            countedSuits.put(card.getSuit(), (count == null) ? 1 : count + 1);
        }

        countedSuits = countedSuits.entrySet().stream()
                .filter(map -> map.getValue() >= COMBINATION_NEED_CARDS)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (countedSuits.isEmpty()) {
            return Collections.emptyList();
        }

        return checkFlushSuit(game, countedSuits.keySet());
    }

    private List<Character> checkFlushSuit(Game game, Set<Character> suits) {
        List<Character> flushSuits = new ArrayList<>();

        for (char suit : suits) {
            long handSuitsCount = Arrays.stream(game.getHandCards())
                    .filter(card -> card.getSuit() == suit)
                    .count();

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
            Arrays.stream(game.getHandCards()).forEach(card -> {
                if (card.getSuit() == suit) {
                    handCards.add(card);
                }
            });

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
        List<Character> faces = combination.stream()
                .map(Card::getFaceValue)
                .sorted()
                .collect(Collectors.toList());

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

        Map<Character, Integer> facesCount = new HashMap<>();
        for (List<Character> combination : combinations) {
            facesCount.clear();

            for (Character face : combination) {
                Integer count = facesCount.get(face);
                facesCount.put(face, (count == null) ? 1 : count + 1);
            }

            if (facesCount.values().contains(FOUR_OF_A_KIND_NEED_CARDS)) {
                return FOUR_OF_A_KIND;
            }

            if (handRank >= RANK_FULL_HOUSE) {
                continue;
            }
            if (facesCount.keySet().size() == DISTINCT_FACES_FOR_FULL_HOUSE) {
                bestHand = FULL_HOUSE;
                handRank = RANK_FULL_HOUSE;
            }

            if (handRank >= RANK_STRAIGHT) {
                continue;
            }
            if (Collections.indexOfSubList(STRAIGHT_SEQUENCE, combination) != -1) {
                bestHand = STRAIGHT;
                handRank = RANK_STRAIGHT;
            }

            if (handRank >= RANK_THREE_OF_A_KIND) {
                continue;
            }
            if (facesCount.values().contains(THREE_OF_A_KIND_NEED_CARDS)) {
                bestHand = THREE_OF_A_KIND;
                handRank = RANK_THREE_OF_A_KIND;
            }

            if (handRank >= RANK_TWO_PAIRS) {
                continue;
            }
            if (Collections.frequency(facesCount.values(), ONE_PAIR_NEED_CARDS) == TWO_PAIRS_NEED_PAIRS) {
                bestHand = TWO_PAIRS;
                handRank = RANK_TWO_PAIRS;
            }

            if (handRank >= RANK_ONE_PAIR) {
                continue;
            }
            if (facesCount.values().contains(ONE_PAIR_NEED_CARDS)) {
                bestHand = ONE_PAIR;
                handRank = RANK_ONE_PAIR;
            }
        }
        return bestHand;
    }
}
