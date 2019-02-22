package by.radchuk.task1.reader;

import by.radchuk.task1.entity.GeometryObject;
import by.radchuk.task1.exception.GeometryException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * This class reads figure data from the specified source.
 * @param <T> any geometry object.
 */
public interface FigureReader<T extends GeometryObject> {
    /**
     * reads the data from the specified file.
     * creates list of figures.
     * @param path path to file with figure data.
     * @return list of cubes
     * @throws IOException in case of problems with reading data.
     * @throws GeometryException in case data is incorrect.
     */
    List<T> read(Path path) throws IOException, GeometryException;
}
