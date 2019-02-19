
package by.radchuk.task1.entity;

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
public abstract class Cube extends GeometryObject {
    /**
     * Cube constructor.
     * @param name name of the object.
     * @param center the center point of three-dimensional cube.
     * @param edge edge length of the cube.
     */
    public Cube(final String name,
                          final Point center, final double edge) {
        super(name);
        this.centerPoint = center;
        this.edgeLength = edge;
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
}



