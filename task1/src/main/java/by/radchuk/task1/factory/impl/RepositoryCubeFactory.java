package by.radchuk.task1.factory.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.repository.FigureRepository;
import by.radchuk.task1.repository.impl.CubeRepository;

/**
 * factory class. creates cube instance from a string. Adds created cube to a
 * repository.
 */
public class RepositoryCubeFactory extends CubeFactory {

	/**
	 * repository reference.
	 */
	private FigureRepository<Cube> repository = CubeRepository.INSTANCE;

	/**
	 * creates cube instance from a string with data.
	 *
	 * @param data cube data
	 * @return cube instance
	 */
	@Override
	public Cube createFigure(final String data) {
		ObservableCube cube = new ObservableCube(super.createFigure(data));
		ObservablePoint centerPoint = new ObservablePoint(cube.getCenterPoint());
		cube.setCenterPoint(centerPoint);
		centerPoint.setObserver(cube);
		cube.setObserver(repository);
		repository.add(cube);
		return cube;
	}
}
