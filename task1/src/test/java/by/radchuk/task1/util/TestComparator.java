package by.radchuk.task1.util;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.Point;

public class TestComparator {
    public static boolean compareCube(Cube lhs, String name, double x, double y, double z, double edgeLength) {
        return lhs.getName().equals(name)
                && Double.compare(lhs.getEdgeLength(), edgeLength) == 0
                && Double.compare(lhs.getCenterPoint().getX(), x) == 0
                && Double.compare(lhs.getCenterPoint().getY(), y) == 0
                && Double.compare(lhs.getCenterPoint().getZ(), z) == 0;
    }

    public static boolean comparePoint(final Point lhs, final String name,
                                final double x, final double y, final double z) {
        return lhs.getName().equals(name)
                && Double.compare(lhs.getX(), x) == 0
                && Double.compare(lhs.getY(), y) == 0
                && Double.compare(lhs.getZ(), z) == 0;
    }
}
