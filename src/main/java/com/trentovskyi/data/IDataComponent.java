package com.trentovskyi.data;

import com.trentovskyi.models.Game;

import java.io.IOException;
import java.util.List;

public interface IDataComponent {

    List<Game> getInputData(String path) throws IOException;

    void output(String path, List<Game> result) throws IOException;
}
