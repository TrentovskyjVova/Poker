package com.trentovskyi;

import com.trentovskyi.models.Card;
import com.trentovskyi.models.FaceValue;
import com.trentovskyi.models.Game;

import java.util.*;
import java.util.stream.Collectors;

import static com.trentovskyi.models.Combination.*;
import static com.trentovskyi.models.FaceValue.STRAIGHT_SEQUENCE;

public class PokerProcessor {

    public List<Game> process(List<Game> inputData) {
        for (Game game : inputData) {
            List<Character> flushPossibleSuits = checkFlushPossible(game);
            if (!flushPossibleSuits.isEmpty()) {
                if (checkStraightFlushPossible(game, flushPossibleSuits)) {
                    game.setBestHand(STRAIGHT_FLUSH);
                    continue;
                } else {
                    game.setBestHand(FLUSH);
                }
            }
            List<List<Character>> allFacesCombinations = findAllFacesCombinations(game);
            checkRestCombinations(game, allFacesCombinations);

        }
        return inputData;
    }

    private List<Character> checkFlushPossible(Game game) {
        Map<Character, Integer> suitsCount = new HashMap<>();
        for (Card c : game.getAllCards()) {
            Integer count = suitsCount.get(c.getSuit());
            suitsCount.put(c.getSuit(), (count == null) ? 1 : count + 1);
        }

        suitsCount = suitsCount.entrySet().stream()
                .filter(map -> map.getValue() >= COMBINATION_NEED_CARDS)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (suitsCount.isEmpty()) {
            return Collections.emptyList();
        }

        return checkFlushSuit(game, suitsCount.keySet());
    }

    private List<Character> checkFlushSuit(Game game, Set<Character> suits) {
        List<Character> result = new ArrayList<>();

        for (Character suit : suits) {
            long handSuitsCount = Arrays.stream(game.getHandCards())
                    .filter(card -> card.getSuit() == suit)
                    .count();

            boolean isAllRightSuits = true;
            for (int i = 0; i < COMBINATION_NEED_CARDS - handSuitsCount; i++) {
                if (game.getDeckCards()[i].getSuit() != suit) {
                    isAllRightSuits = false;
                    break;
                }
            }
            if (isAllRightSuits) {
                result.add(suit);
            }
        }
        return result;
    }

    private boolean checkStraightFlushPossible(Game game, List<Character> flushPossibleSuits) {
        List<List<Character>> combinations = prepareFlushCombinations(game, flushPossibleSuits);

        for (List<Character> combination : combinations) {
            if (Collections.indexOfSubList(STRAIGHT_SEQUENCE, combination) != -1) {
                return true;
            }
        }

        return false;
    }

    private List<List<Character>> prepareFlushCombinations(Game game, List<Character> flushPossibleSuits) {
        List<List<Character>> combinations = new ArrayList<>();

        for (Character suit : flushPossibleSuits) {
            List<Card> handCards = new ArrayList<>();
            Arrays.stream(game.getHandCards()).forEach(card -> {
                if (card.getSuit() == suit) {
                    handCards.add(card);
                }
            });

            List<List<Card>> handCardsPawerset = powerset(handCards);
            Collections.reverse(handCardsPawerset);
            for (List<Card> combination : handCardsPawerset) {
                // add desk cards
                int cardsFromDeck = COMBINATION_NEED_CARDS - combination.size();
                for (int i = 0; i < cardsFromDeck; i++) {
                    if (game.getDeckCards()[i].getSuit() == suit) {
                        combination.add(game.getDeckCards()[i]);
                    }
                }

                //check
                if (combination.size() == COMBINATION_NEED_CARDS) {
                    List<Character> faces = combination.stream()
                            .sorted(Comparator.comparing(a -> a.getFaceValue().getValue()))
                            .map(card -> card.getFaceValue().getFace())
                            .collect(Collectors.toList());

                    combinations.add(faces);

                    if (faces.get(0) == FaceValue.ACE) {
                        List<Character> aceReverse = new ArrayList<>(faces.subList(1, COMBINATION_NEED_CARDS));
                        aceReverse.add(FaceValue.ACE);
                        combinations.add(aceReverse);
                    }
                }
            }
        }
        return combinations;
    }

    private <T> List<List<T>> powerset(Collection<T> list) {
        List<List<T>> ps = new ArrayList<List<T>>();
        ps.add(new ArrayList<T>());

        for (T item : list) {
            List<List<T>> newPs = new ArrayList<List<T>>();

            for (List<T> subset : ps) {
                newPs.add(subset);

                List<T> newSubset = new ArrayList<T>(subset);
                newSubset.add(item);
                newPs.add(newSubset);
            }

            ps = newPs;
        }
        return ps;
    }

    private List<List<Character>> findAllFacesCombinations(Game game) {
        List<List<Character>> combinations = new ArrayList<>();

        List<List<Card>> handCardsPawerset = powerset(Arrays.asList(game.getHandCards()));
        Collections.reverse(handCardsPawerset);
        for (List<Card> combination : handCardsPawerset) {
            int cardsFromDeck = COMBINATION_NEED_CARDS - combination.size();
            for (int i = 0; i < cardsFromDeck; i++) {
                combination.add(game.getDeckCards()[i]);
            }

            List<Character> faces = combination.stream()
                    .sorted(Comparator.comparing(a -> a.getFaceValue().getValue()))
                    .map(card -> card.getFaceValue().getFace())
                    .collect(Collectors.toList());

            combinations.add(faces);

            if (faces.get(0) == FaceValue.ACE) {
                List<Character> aceReverse = new ArrayList<>(faces.subList(1, COMBINATION_NEED_CARDS));
                aceReverse.add(FaceValue.ACE);
                combinations.add(aceReverse);
            }
        }
        return combinations;
    }

    private boolean checkRestCombinations(Game game, List<List<Character>> combinations) {

        Map<Character, Integer> facesCount = new HashMap<>();
        for (List<Character> combination : combinations) {
            facesCount.clear();

            for (Character faces : combination) {
                Integer count = facesCount.get(faces);
                facesCount.put(faces, (count == null) ? 1 : count + 1);
            }

            if (facesCount.values().contains(FOUR_OF_A_KIND_NEED_CARDS)) {
                game.setBestHand(FOUR_OF_A_KIND);
                return true;
            }

            if (noNeedSearchLowerCombination(game.getBestHand(), FULL_HOUSE)) {
                continue;
            }

            if (facesCount.keySet().size() == DISTINCT_FACES_FOR_FULL_HOUSE) {
                game.setBestHand(FULL_HOUSE);
            }

            if (noNeedSearchLowerCombination(game.getBestHand(), STRAIGHT)) {
                continue;
            }

            if (Collections.indexOfSubList(STRAIGHT_SEQUENCE, combination) != -1) {
                game.setBestHand(STRAIGHT);
            }

            if (noNeedSearchLowerCombination(game.getBestHand(), THREE_OF_A_KIND)) {
                continue;
            }

            if (facesCount.values().contains(THREE_OF_A_KIND_NEED_CARDS)) {
                game.setBestHand(THREE_OF_A_KIND);
            }

            if (noNeedSearchLowerCombination(game.getBestHand(), TWO_PAIRS)) {
                continue;
            }

            if (Collections.frequency(facesCount.values(), ONE_PAIR_NEED_CARDS) == TWO_PAIRS_NEED_PAIRS) {
                game.setBestHand(TWO_PAIRS);
            }

            if (noNeedSearchLowerCombination(game.getBestHand(), ONE_PAIR)) {
                continue;
            }

            if (facesCount.values().contains(ONE_PAIR_NEED_CARDS)) {
                game.setBestHand(ONE_PAIR);
            }

        }


        return false;
    }

    private boolean noNeedSearchLowerCombination(String old, String candidate) {
        return COMBINATION_SEQUENCE.indexOf(old) <= COMBINATION_SEQUENCE.indexOf(candidate);
    }
}
