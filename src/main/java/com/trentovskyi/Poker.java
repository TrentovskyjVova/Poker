package com.trentovskyi;

import com.trentovskyi.data.FileDataComponent;
import com.trentovskyi.data.IDataComponent;
import com.trentovskyi.models.Game;
import com.trentovskyi.processor.IProcessor;
import com.trentovskyi.processor.PokerProcessor;

import java.io.IOException;
import java.util.List;

public class Poker {

    private static final String INPUT_FILE_NAME = "./res/Input.txt";
    private static final String OUTPUT_FILE_NAME = "./out/Output.txt";

    public static void main(String[] args) {
        IDataComponent dataComponent = new FileDataComponent();

        List<Game> games;
        try {
            games = dataComponent.getInputData(INPUT_FILE_NAME);
        } catch (IOException e) {
            System.out.println("Bed input. " + e.getMessage());
            return;
        }

        IProcessor processor = new PokerProcessor();
        processor.process(games);

        try {
            dataComponent.output(OUTPUT_FILE_NAME, games);
        } catch (IOException e) {
            System.out.println("Can't output result. " + e.getMessage());
        }
    }


}
