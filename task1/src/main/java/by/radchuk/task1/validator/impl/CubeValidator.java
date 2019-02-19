package by.radchuk.task1.validator.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.validator.FigureValidator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * validates cube data correctness.
 */
@Slf4j
public class CubeValidator implements FigureValidator<Cube> {
    /**
     * status message.
     */
    @Getter
    private String statusMessage = "OK";
    /**
     * validates cube data correctness.
     * @param figure cube to validate.
     * @return true if OK, otherwise false.
     */
    @Override
    public boolean validate(final Cube figure) {
        if (figure.getEdgeLength() <= 0) {
            statusMessage = "Cube edge length can't be less than zero!";
            log.debug(
                    "Cube validation failed with statusMessage={}",
                    statusMessage);
            return false;
        }
        return true;
    }
}
