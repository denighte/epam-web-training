package by.radchuk.task1.factory;

import by.radchuk.task1.entity.GeometryObject;
import by.radchuk.task1.exception.GeometryException;

/**
 * factory method class.
 * creates figure instance from a string.
 * @param <T> any geometry object
 */
public interface FigureFactory<T extends GeometryObject> {
    /**
     * creates cube instance from a string with data.
     * @param data cube data.
     * @return cube instance.
     * @throws GeometryException in case data is incorrect.
     */
    T createFigure(String data) throws GeometryException;
}
