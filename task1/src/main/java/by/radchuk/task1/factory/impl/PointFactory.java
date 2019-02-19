package by.radchuk.task1.factory.impl;

import by.radchuk.task1.entity.Point;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * factory method.
 * creates Point instance from a string with data.
 */
@Slf4j
public class PointFactory implements FigureFactory<Point> {
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
     * Point data string pattern.
     */
    private static final Pattern POINT_PATTERN = Pattern.compile(
            "(\\w+):"
                    + "\\(([+-]?\\d+(?:\\.\\d+)?),"
                    + "([+-]?\\d+(?:\\.\\d+)?),"
                    + "([+-]?\\d+(?:\\.\\d+)?)\\)"
    );
    /**
     * creates point instance from a string with data.
     * @param data point data
     * @return point instance
     */
    @Override
    public Point createFigure(final String data) {
        String formatData = data.replaceAll("\\s+", "");

        Matcher matcher = POINT_PATTERN.matcher(formatData);
        if (!matcher.matches()) {
            log.error(
                    "the string didn't matched regex with point data = {}",
                    data
            );
            throw new GeometryException(
                    "invalid format of string with point data!"
            );
        }

        double x = Double.parseDouble(matcher.group(X_GROUP));
        double y = Double.parseDouble(matcher.group(Y_GROUP));
        double z = Double.parseDouble(matcher.group(Z_GROUP));

        return new Point(matcher.group(NAME_GROUP), x, y, z);
    }
}
