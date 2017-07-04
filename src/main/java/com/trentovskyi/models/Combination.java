package com.trentovskyi.models;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vtrentovskyy on 02.07.2017.
 */
public class Combination {

    public static final int COMBINATION_NEED_CARDS = 5;
    public static final int FLUSH_NEED_CARDS = 5;
    public static final int FOUR_OF_A_KIND_NEED_CARDS = 4;
    public static final int THREE_OF_A_KIND_NEED_CARDS = 3;
    public static final int DISTINCT_FACES_FOR_FULL_HOUSE = 2;
    public static final int TWO_PAIRS_NEED_PAIRS = 2;
    public static final int ONE_PAIR_NEED_CARDS = 2;

    public static final String STRAIGHT_FLUSH = "straight-flush";
    public static final String FOUR_OF_A_KIND = "four-of-a-kind";
    public static final String FULL_HOUSE = "full-house";
    public static final String FLUSH = "flush";
    public static final String STRAIGHT = "straight";
    public static final String THREE_OF_A_KIND = "three-of-a-kind";
    public static final String TWO_PAIRS = "two-pairs";
    public static final String ONE_PAIR = "one-pair";
    public static final String HIGHEST_CARD = "highest-card";

    public static final List<String> COMBINATION_SEQUENCE = Arrays.asList(
            STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE, FLUSH,
            STRAIGHT, THREE_OF_A_KIND, TWO_PAIRS, ONE_PAIR, HIGHEST_CARD
    );
}
