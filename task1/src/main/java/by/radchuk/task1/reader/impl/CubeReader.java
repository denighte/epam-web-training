package by.radchuk.task1.reader.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.reader.FigureReader;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
/**
 * This class reads figure data from file.
 */
@Slf4j
@AllArgsConstructor
public class CubeReader implements FigureReader<Cube> {
    /**
     * cube factory, which creates cubes.
     */
    private FigureFactory<Cube> factory;

    /**
     * reads the data from the specified file.
     * creates list of cubes, created by specified factory.
     * @param path path to file with figure data.
     * @return list of cubes
     * @throws IOException in case of problems with reading data.
     * @throws GeometryException in case data is incorrect.
     */
    @Override
    public List<Cube> read(final Path path)
            throws IOException, GeometryException {
        log.info("Reading cube data from file with name = {} ...",
                path.getFileName());

        List<Cube> list = new ArrayList<>();
        for (String data : Files.readAllLines(path)) {
            list.add(factory.createFigure(data));
        }

        log.info("Finished reading data from file with name = {}",
                path.getFileName());

        return list;
    }
}
