package com.trentovskyi.data;

import com.trentovskyi.models.Card;
import com.trentovskyi.models.Game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE;

public class FileDataComponent implements IDataComponent {

    @Override
    public List<Game> getInputData(String path) throws IOException {
        Stream<String> input = Files.lines(Paths.get(path));

        return input
                .map(s -> {
                    String[] cards = s.trim().split(" ");
                    Card[] cards1 = Arrays.stream(cards).map(Card::new).toArray(Card[]::new);
                    return new Game(cards1);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void output(String path, List<Game> result) throws IOException {
        Iterable<String> lines = result.stream()
                .map(Game::toString)
                .collect(Collectors.toList());

        Files.write(Paths.get(path), lines, CREATE);
    }
}
