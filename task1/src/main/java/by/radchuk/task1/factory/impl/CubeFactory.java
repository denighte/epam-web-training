package by.radchuk.task1.factory.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.Point;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.validator.FigureValidator;
import by.radchuk.task1.validator.impl.CubeValidator;
import lombok.extern.slf4j.Slf4j;

/**
 * Factory method.
 * creates cube instance from a string with data.
 */
@Slf4j
public class CubeFactory implements FigureFactory<Cube> {
    /**
     * name regex group.
     */
    private static final int NAME_GROUP = 1;
    /**
     * x coordinate regex group.
     */
    private static final int X_GROUP = 2;
    /**
     * y coordinate regex group.
     */
    private static final int Y_GROUP = 3;
    /**
     * z coordinate regex group.
     */
    private static final int Z_GROUP = 4;
    /**
     * edge length regex group.
     */
    private static final int EDGE_GROUP = 5;
    /**
     * cube data string pattern.
     */
    private static final Pattern CUBE_PATTERN =
            Pattern.compile(
                    "(\\w+):"
                            + "\\{\\(([+-]?\\d+(?:\\.\\d+)?),"
                            + "([+-]?\\d+(?:\\.\\d+)?),"
                            + "([+-]?\\d+(?:\\.\\d+)?)\\),"
                            + "([+-]?\\d+(?:\\.\\d+)?)\\}"
            );

    /**
     * creates cube instance from a string with data.
     * @param data cube data
     * @return cube instance
     */
    @Override
    public Cube createFigure(final String data) {
        String formatData = data.replaceAll("\\s+", "");

        Matcher matcher = CUBE_PATTERN.matcher(formatData);
        if (!matcher.matches()) {
            log.error(
                    "the string didn't matched regex with cube data = {}",
                    data
            );
            throw new GeometryException(
                    "invalid format of string with cube data!"
            );
        }

        double x = Double.parseDouble(matcher.group(X_GROUP));
        double y = Double.parseDouble(matcher.group(Y_GROUP));
        double z = Double.parseDouble(matcher.group(Z_GROUP));
        double edgeLength = Double.parseDouble(matcher.group(EDGE_GROUP));

        Cube cube = new Cube(
                matcher.group(NAME_GROUP),
                new Point(matcher.group(NAME_GROUP), x, y, z),
                edgeLength
        );

        FigureValidator<Cube> validator = new CubeValidator();
        if (!validator.validate(cube)) {
            throw new GeometryException(validator.getStatusMessage());
        }

        return new CheckedCube(cube);
    }
}
