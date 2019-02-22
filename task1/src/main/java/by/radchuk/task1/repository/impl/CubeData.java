package by.radchuk.task1.repository.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * contains cube computable data.
 */
@Data
@Slf4j
class CubeData {
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
     */
    private double[] axisCutVolumeRatio;
    /**
     * is cube face on any coordinate plane.
     */
    private boolean isOnCoordinatePlane;
}


