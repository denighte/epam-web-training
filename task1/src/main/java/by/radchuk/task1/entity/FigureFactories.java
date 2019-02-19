package by.radchuk.task1.entity;

import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.CubeFactory;

public class FigureFactories {

	public FigureFactories() {
		// TODO Auto-generated constructor stub
	}

	FigureFactory<Cube> newCubeFactory() {
		return new CubeFactory();
	}
}
