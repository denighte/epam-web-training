package by.radchuk.task1.action;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.CubeFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

//TODO: add tests on other methods
public class CubeMathTest {
    private FigureFactory<Cube> factory;
    private CubeMath math;
    private Cube cube;

    @BeforeClass
    public void setUp() {
        factory = new CubeFactory();
        math = new CubeMath();
    }

    @DataProvider
    public Object[][] areaTestProvider() {
        return new Object[][] {
                {"Cube1: {(1, 5, 8), 10}", 100*6},
                {"Cube2: {(1, -2, 3.5), 5.5}", 5.5*5.5*6},
                {"Cube3: {(-1.1, 33, -8.99), 0.01}", 0.01*0.01*6}
        };
    }

    @DataProvider
    public Object[][] volumeTestProvider() {
        return new Object[][] {
                {"Cube1: {(1, 5, 8), 10}", 10*10*10},
                {"Cube2: {(1, -2, 3.5), 5.5}", 5.5*5.5*5.5},
                {"Cube3: {(-1.1, 33, -8.99), 0.01}", 0.01*0.01*0.01}
        };
    }

    @Test(dataProvider = "areaTestProvider")
    public void areaTest(String data, double expected) throws GeometryException {
        Cube cube = factory.createFigure(data);
        double actual = math.surfaceArea(cube);
        Assert.assertEquals(Double.compare(actual, expected), 0);
    }

    @Test(dataProvider = "volumeTestProvider")
    public void volumeTest(String data, double expected) throws GeometryException {
        Cube cube = factory.createFigure(data);
        double actual = math.volume(cube);
        Assert.assertEquals(Double.compare(actual, expected), 0);
    }
}
