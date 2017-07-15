package com.trentovskyi.data;

import com.trentovskyi.models.Card;
import com.trentovskyi.models.Game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileDataComponent implements IDataComponent {

    private static final int ALL_GAME_CARDS_COUNT = 10;
    private static final int BYTES_FOR_ONE_CARD = 3;

    @Override
    public List<Game> getInputData(String path) throws IOException {
        List<Game> games = new ArrayList<>();
        String line;
        try (
                InputStream inputStream = new FileInputStream(path);
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((line = br.readLine()) != null) {
                Card[] cards = new Card[ALL_GAME_CARDS_COUNT];
                for (int i = 0; i < ALL_GAME_CARDS_COUNT; i++) {
                    cards[i] = Card.getInstance(
                            line.charAt(i * BYTES_FOR_ONE_CARD),
                            line.charAt(i * BYTES_FOR_ONE_CARD + 1));
                }
                games.add(new Game(cards));
            }
        }
        return games;
    }

    @Override
    public void output(String path, List<Game> result) throws IOException {
        PrintWriter printWriter = new PrintWriter(path);
        for (Game game : result) {
            printWriter.println(game);
        }
        printWriter.close();
    }
}
