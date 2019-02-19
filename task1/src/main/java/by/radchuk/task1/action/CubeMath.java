package by.radchuk.task1.action;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.Point;


/**
 * This class implements math cube operations.
 */
public final class CubeMath {
    /**
     * Number of cube faces.
     */
    private static final int CUBE_FACES_NUMBER = 6;
    /**
     * calculates surface area of the cube.
     * @param cube object to calculate.
     * @return cube surface area.
     */
    public double surfaceArea(final Cube cube) {
        double edgeLength = cube.getEdgeLength();
        return edgeLength * edgeLength * CUBE_FACES_NUMBER;
    }

    /**
     * calculates volume of the cube.
     * @param cube object to calculate
     * @return cube volume
     */
    public double volume(final Cube cube) {
        double edgeLength = cube.getEdgeLength();
        return edgeLength * edgeLength * edgeLength;
    }

    /**
     * checks whether the cube is located on the coordinate plane.
     * @param cube object to check
     * @return true, if cube located on coordinate plane, otherwise else.
     */
    public boolean isOnCoordinatePlane(final Cube cube) {
        Point centerPoint = cube.getCenterPoint();
        double edgeLength = cube.getEdgeLength();
        return (Math.abs(centerPoint.getX()) == edgeLength / 2)
                || (Math.abs(centerPoint.getY()) == edgeLength / 2)
                || (Math.abs(centerPoint.getZ()) == edgeLength / 2);
    }

    /**
     * calculates axis cut volume ratio.
     * @param cube object to calculate.
     * @return array of cut volumes.
     */
    public double[] axisCutVolumeRatio(final Cube cube) {
        //TODO: add implementation
        return new double[]{};
    }
}
