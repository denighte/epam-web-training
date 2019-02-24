package by.radchuk.task1.entity;

/**
 * Computable parameters of the cube.
 */
public interface CubeParameters {
    /**
     * get surface area of the cube.
     * @return surface area of the cube.
     */
    double getSurfaceArea();

    /**
     * get volume of the cube.
     * @return volume of the cube.
     */
    double getVolume();

    /**
     * check if cube stands on coordinate plane.
     * @return true, if stands, otherwise false.
     */
    boolean isOnCoordinatePlane();

    /**
     * get cube axis cut ratio.
     * @return cube axis cut ratio array,
     * contains octant volume ratio.
     * first element of the array - first octant
     * ... and so on.
     */
    double[] getAxisCutVolumeRatio();
}
