
package by.radchuk.task1.entity;

import by.radchuk.task1.exception.GeometryException;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * entity class.
 * implements a geometry Cube model in three-dimensional space.
 * it expected that cube is not rotated in space.
 * all the logic depends on that fact.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Cube extends GeometryObject {
    /**
     * protected Cube constructor to prevent unchecked instance creation.
     * @param name name of the object.
     * @param center the center point of three-dimensional cube.
     * @param edge edge length of the cube.
     */
    protected Cube(final String name,
                          final Point center, final double edge) {
        super(name);
        centerPoint = center;
        edgeLength = edge;
    }

    /**
     * Copy constructor.
     * @param cube other cube.
     */
    public Cube(final Cube cube) {
        super(cube.getName());
        centerPoint = cube.centerPoint;
        edgeLength = cube.edgeLength;
    }
    /**
     * the center point of three-dimensional cube.
     */
    private Point centerPoint;
    /**
     * edge length of the cube.
     */
    private double edgeLength;

    /**
     * special edge length setter to prevent unchecked changes.
     * @param length new edge length.
     * @throws GeometryException throw in case of an incorrect data change.
     */
    public void setEdgeLength(final double length)
            throws GeometryException {
        edgeLength = length;
    }

    /**
     * special center point setter to prevent unchecked changes.
     * @param point new center point
     * @throws GeometryException throw in case of an incorrect data change.
     */
    protected void setCenterPoint(final Point point)
            throws GeometryException {
        centerPoint = point;
    }
}



