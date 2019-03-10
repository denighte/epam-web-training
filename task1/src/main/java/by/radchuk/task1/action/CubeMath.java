package by.radchuk.task1.action;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.Point;


/**
 * This class implements math cube operations.
 */
public final class CubeMath {
    /**
     * octants number.
     */
    private static final int OCTANTS_NUMBER = 8;
    /**
     * first octant.
     */
    private static final int FIRST_OCTANT = 0;
    /**
     * second octant.
     */
    private static final int SECOND_OCTANT = 1;
    /**
     * third octant.
     */
    private static final int THIRD_OCTANT = 2;
    /**
     * fourth octant.
     */
    private static final int FOURTH_OCTANT = 3;
    /**
     * fifth octant.
     */
    private static final int FIFTH_OCTANT = 4;
    /**
     * sixth octant.
     */
    private static final int SIXTH_OCTANT = 5;
    /**
     * seventh octant.
     */
    private static final int SEVENTH_OCTANT = 6;
    /**
     * eighth octant.
     */
    private static final int EIGHTH_OCTANT = 7;

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
        double[] axisCut = new double[OCTANTS_NUMBER];
        double x = cube.getCenterPoint().getX();
        double y = cube.getCenterPoint().getY();
        double z = cube.getCenterPoint().getZ();
        double edgeLength = cube.getEdgeLength();
        double halfEdge = edgeLength / 2;
        axisCut[FIRST_OCTANT] = partVolume(0, 0, 0,
                x + halfEdge, y + halfEdge, z + halfEdge);
        axisCut[SECOND_OCTANT] = partVolume(x - halfEdge, 0, 0,
                0, y + halfEdge, z + halfEdge);
        axisCut[THIRD_OCTANT] = partVolume(x - halfEdge, y - halfEdge, 0,
                0, 0, z + halfEdge);
        axisCut[FOURTH_OCTANT] = partVolume(0, y - halfEdge, 0,
                x + halfEdge, 0, z + halfEdge);
        axisCut[FIFTH_OCTANT] = partVolume(0, 0, z - halfEdge,
                x + halfEdge, y + halfEdge, 0);
        axisCut[SIXTH_OCTANT] = partVolume(x - halfEdge, 0, z - halfEdge,
                0, y + halfEdge, 0);
        axisCut[SEVENTH_OCTANT] = partVolume(
                x - halfEdge, y - halfEdge, z - halfEdge,
                0, 0, 0);
        axisCut[EIGHTH_OCTANT] = partVolume(0, y - halfEdge, z - halfEdge,
                x + halfEdge, 0, 0);
        return axisCut;
    }

    /**
     * calculates rectangle volume.
     * @param x0 left bottom point x.
     * @param y0 left bottom point y.
     * @param z0 left bottom point z.
     * @param x1 right upper point x.
     * @param y1 right upper point y.
     * @param z1 right upper point z.
     * @return rectangle volume.
     */
    private double partVolume(
                       final double x0,
                       final double y0,
                       final double z0,
                       final double x1,
                       final double y1,
                       final double z1) {
        if (x1 < x0 || y1 < y0 || z1 < y0) {
            return 0;
        }
        return (x1 - x0) * (y1 - y0) * (z1 - z0);
    }
}
