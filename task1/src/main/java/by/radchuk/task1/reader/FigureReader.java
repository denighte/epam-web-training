package by.radchuk.task1.reader;

import java.io.IOException;
import java.util.List;

/**
 * This class reads figure data from the specified source.
 */
public interface FigureReader {
    /**
     * reads the data from the specified source.
     * @return List of string, containing figure data.
     * @throws IOException reading source exception.
     */
    List<String> read() throws IOException;
}
