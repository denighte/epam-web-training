package by.radchuk.task1.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * entity class.
 * implements a geometry Point model in three-dimensional space.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Point extends GeometryObject {
    /**
     * protected Point constructor to prevent unchecked instance creation.
     * @param name name of the object
     * @param newX x axis coordinate.
     * @param newY y axis coordinate.
     * @param newZ z axis coordinate.
     */
    protected Point(final String name,
                 final double newX,
                 final double newY,
                 final double newZ) {
        super(name);
        this.x = newX;
        this.y = newY;
        this.z = newZ;
    }

    /**
     * copy constructor.
     * @param point other point
     */
    public Point(final Point point) {
        super(point.getName());
        x = point.x;
        y = point.y;
        z = point.z;
    }
    /**
     * x axis coordinate.
     */
    private double x;
    /**
     * y axis coordinate.
     */
    private double y;
    /**
     * z axis coordinate.
     */
    private double z;
}
