package com.trentovskyi.processor;

import com.trentovskyi.models.Game;

import java.util.List;

public interface IProcessor {

    void process(List<Game> inputData);
}
