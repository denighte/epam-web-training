package by.radchuk.task1.reader.impl;

import by.radchuk.task1.reader.FigureReader;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class reads figure data from file.
 */
@AllArgsConstructor
public class FigureFileReader implements FigureReader {
    /**
     * path to file with figure data.
     */
    private Path path;

    /**
     * reads the data from the specified file.
     * @return List of string, containing figure data
     * @throws IOException reading file exception
     */
    @Override
    public List<String> read() throws IOException {
        return Files.readAllLines(path);
    }
}
