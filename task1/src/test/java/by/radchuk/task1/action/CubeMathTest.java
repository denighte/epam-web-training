package by.radchuk.task1.action;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.CubeFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CubeMathTest {
    /**
     * Cube factory.
     */
    private FigureFactory<Cube> factory;
    /**
     * Cube math.
     */
    private CubeMath math;

    /**
     * Test resources initialization.
     */
    @BeforeClass
    public void setUp() {
        factory = new CubeFactory();
        math = new CubeMath();
    }

    /**
     * test provider for area() method
     * @return test data
     */
    @DataProvider
    public Object[][] areaTestProvider() {
        return new Object[][] {
                {"Cube1: {(1, 5, 8), 10}", 100*6},
                {"Cube2: {(1, -2, 3.5), 5.5}", 5.5*5.5*6},
                {"Cube3: {(-1.1, 33, -8.99), 0.01}", 0.01*0.01*6}
        };
    }

    /**
     * test provider for isOnCoordinatePlane() method
     * @return test data
     */
    @DataProvider
    public Object[][] isOnCoordinatePlaneProvider() {
        return new Object[][] {
                {"Cube1: {(2, 2, 2), 4}", true},
                {"Cube2: {(1, -2, 1), 4}", true},
                {"Cube3: {(-1.1, 33, -8.99), 17.98}", true},
                {"Cube4: {(-5.5, 13.1, 18), 20.2}", false},
                {"Cube5: {(-4, -2.2, 8.5), 17}", true}
        };
    }

    /**
     * test provider for volume() method
     * @return test data
     */
    @DataProvider
    public Object[][] volumeTestProvider() {
        return new Object[][] {
                {"Cube1: {(1, 5, 8), 10}", 10*10*10},
                {"Cube2: {(1, -2, 3.5), 5.5}", 5.5*5.5*5.5},
                {"Cube3: {(-1.1, 33, -8.99), 0.01}", 0.01*0.01*0.01}
        };
    }

    /**
     * test provider for axisCutVolumeRatio() method
     * @return test data
     */
    @DataProvider
    public Object[][] axisCutTestProvider() {
        return new Object[][] {
                {
                    "Cube1: {(0, 0, 0), 4}",
                    new double[] {8, 8, 8, 8, 8, 8, 8, 8}
                },
                {
                    "Cube2: {(1, 1, 1), 4}",
                    new double[] {27, 9, 3, 9, 9, 3, 1, 3}
                },
                {
                    "Cube3: {(2, 0, 0), 4}",
                    new double[] {16, 0, 0, 16, 16, 0, 0, 16}
                }
        };
    }

    /**
     * area test.
     * @param data cube data.
     * @param expected expected value.
     * @throws GeometryException standard exception for geometry classes.
     */
    @Test(dataProvider = "areaTestProvider")
    public void areaTest(String data, double expected)
            throws GeometryException {
        Cube cube = factory.createFigure(data);
        double actual = math.surfaceArea(cube);
        Assert.assertEquals(Double.compare(actual, expected), 0);
    }

    /**
     * volume test.
     * @param data cube data.
     * @param expected expected value.
     * @throws GeometryException standard exception for geometry classes.
     */
    @Test(dataProvider = "volumeTestProvider")
    public void volumeTest(String data, double expected)
            throws GeometryException {
        Cube cube = factory.createFigure(data);
        double actual = math.volume(cube);
        Assert.assertEquals(Double.compare(actual, expected), 0);
    }

    /**
     * isOnCoordinatePlane test.
     * @param data cube data.
     * @param expected expected value.
     * @throws GeometryException standard exception for geometry classes.
     */
    @Test(dataProvider = "isOnCoordinatePlaneProvider")
    public void isOnCoordinatePlaneTest(String data, boolean expected)
            throws GeometryException {
        Cube cube = factory.createFigure(data);
        boolean actual = math.isOnCoordinatePlane(cube);
        Assert.assertEquals(actual, expected);
    }

    /**
     * axisCutVolumeRatio test.
     * @param data cube data.
     * @param expected expected value.
     * @throws GeometryException standard exception for geometry classes.
     */
    @Test(dataProvider = "axisCutTestProvider")
    public void axisCutVolumeRatioTest(String data, double[] expected) throws GeometryException {
        Cube cube = factory.createFigure(data);
        double[] actual = math.axisCutVolumeRatio(cube);
        Assert.assertEquals(actual, expected);
    }
}
