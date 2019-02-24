package by.radchuk.task1.factory.imp;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.CubeFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CheckedCubeTest {
    private FigureFactory<Cube> factory;

    @BeforeClass
    public void setUp() {
        factory = new CubeFactory();
    }

    @Test(expectedExceptions = {GeometryException.class})
    public void setEdgeLengthZeroTest() throws GeometryException {
        Cube cube = factory.createFigure("Cube: {(1, -1, 1.5), 10}");
        cube.setEdgeLength(0);
    }

    @Test
    public void setEdgeLengthNegativeTest() throws GeometryException {
        Cube cube = factory.createFigure("Cube: {(1, -1, 1.55), 15}");
        cube.setEdgeLength(-5);
    }
}
