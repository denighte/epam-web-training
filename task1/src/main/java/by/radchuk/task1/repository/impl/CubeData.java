package by.radchuk.task1.repository.impl;

import by.radchuk.task1.entity.CubeParameters;
import lombok.Data;

/**
 * contains cube computable data.
 */
@Data
class CubeData implements CubeParameters {
    /**
     * data id.
     */
    private int id;
    /**
     * surface area of the cube.
     */
    private double surfaceArea;
    /**
     * volume of the cube.
     */
    private double volume;
    /**
     * cube axis cut ratio.
     * contains octant volume ratio.
     * first element of the array - first octant
     * ... and so on.
     */
    private double[] axisCutVolumeRatio;
    /**
     * is cube face on any coordinate plane.
     */
    private boolean isOnCoordinatePlane;
}


