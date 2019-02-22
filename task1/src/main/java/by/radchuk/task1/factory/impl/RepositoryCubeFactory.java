package by.radchuk.task1.factory.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.repository.FigureRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * factory class.
 * creates cube instance from a string.
 * Adds created cube to a repository.
 */
@Slf4j
@AllArgsConstructor
public class RepositoryCubeFactory extends CubeFactory {
    {
        log.info("Initialising new RepositoryCubeFactory");
    }

    /**
     * repository reference.
     */
    @Setter
    @Getter
    private FigureRepository<Cube> repository;

    /**
     * creates cube instance from a string with data.
     *
     * @param data cube data
     * @return cube instance
     */
    @Override
    public Cube createFigure(final String data) throws GeometryException {
        ObservableCube cube = new ObservableCube(super.createFigure(data));
        ObservablePoint centerPoint =
                new ObservablePoint(cube.getCenterPoint());
        cube.setCenterPoint(centerPoint);
        centerPoint.setObserver(cube);
        cube.setObserver(repository);
        repository.add(cube);
        return cube;
    }
}
