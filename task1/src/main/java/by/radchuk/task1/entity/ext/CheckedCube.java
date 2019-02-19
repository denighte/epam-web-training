package by.radchuk.task1.entity.ext;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.validator.FigureValidator;
import by.radchuk.task1.validator.impl.CubeValidator;
import lombok.extern.slf4j.Slf4j;

/**
 * Cube decorator
 * validates every change of the cube for correctness.
 */
@Slf4j
public class CheckedCube extends Cube {
    /**
     * Cube validator.
     */
    private FigureValidator<Cube> validator = new CubeValidator();

    /**
     * CheckedCube decorator constructor.
     * @param cube base class
     */
    public CheckedCube(final Cube cube) {
        super(cube);
    }

    /**
     * sets new edge length and checks cube data for correctness.
     * @param edgeLength edge length
     */
    @Override
    public void setEdgeLength(final double edgeLength) {
        if (!validator.validate(this)) {
            log.warn(
                    "try to set invalid edge length in cube with id={}",
                    this.getId());
            throw new GeometryException(validator.getStatusMessage());
        }
        super.setEdgeLength(edgeLength);
    }
}


