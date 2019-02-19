package by.radchuk.task1.validator;

import by.radchuk.task1.entity.GeometryObject;

/**
 * validates correctness of the figure.
 * @param <T> figure.
 */
public interface FigureValidator<T extends GeometryObject> {
    /**
     * validates correctness of the figure.
     * @param figure figure to validate
     * @return true if OK, otherwise false
     */
    boolean validate(T figure);

    /**
     * returns status message of the validation.
     * @return status message of the validation
     */
    String getStatusMessage();
}
