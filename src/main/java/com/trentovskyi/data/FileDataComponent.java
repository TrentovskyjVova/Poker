package com.trentovskyi.data;

import com.trentovskyi.models.Card;
import com.trentovskyi.models.Game;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;

public class FileDataComponent implements IDataComponent {

    private static final int ALL_GAME_CARDS_COUNT = 10;

    @Override
    public List<Game> getInputData(String path) throws IOException {
        List<Game> games = new ArrayList<>();
        String line;
        try (
                InputStream inputStream = new FileInputStream(path);
                InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader br = new BufferedReader(isr);
        ) {
            while ((line = br.readLine()) != null) {
                String[] cardRepresentations = line.split(" ");
                Card[] cards = new Card[ALL_GAME_CARDS_COUNT];
                for (int i = 0; i < ALL_GAME_CARDS_COUNT; i++) {
                    cards[i] = Card.getInstance(cardRepresentations[i]);
                }
                games.add(new Game(cards));
            }
        }
        return games;
    }

    @Override
    public void output(String path, List<Game> result) throws IOException {
        Iterable<String> lines = result.stream()
                .map(Game::toString)
                .collect(Collectors.toList());

        Files.write(Paths.get(path), lines, CREATE);
    }
}
