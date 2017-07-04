package com.trentovskyi;

import com.trentovskyi.data.IDataComponent;
import com.trentovskyi.models.Game;
import com.trentovskyi.data.FileDataComponent;

import java.io.IOException;
import java.util.*;

public class Poker {

    private static final String INPUT_FILE_NAME = "./res/Input.txt";
    private static final String OUTPUT_FILE_NAME = "./res/Output.txt";

    public static void main(String[] args) {
        IDataComponent dataComponent = new FileDataComponent();

        List<Game> inputData;
        try {
            inputData = dataComponent.getInputData(INPUT_FILE_NAME);
        } catch (IOException e) {
            System.out.println("Bed input. " + e.getMessage());
            return;
        }

        PokerProcessor processor = new PokerProcessor();
        List<Game> result = processor.process(inputData);

        try {
            dataComponent.output(OUTPUT_FILE_NAME, result);
        } catch (IOException e) {
            System.out.println("Can't output result. " + e.getMessage());
        }

    }


}
