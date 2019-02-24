package by.radchuk.task1.factory.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.util.TestComparator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CubeFactoryTest {
    private FigureFactory<Cube> factory;

    @DataProvider
    public Object[][] generalTestProvider() {
        return new Object[][]{
                {
                    "cube1: {(1, 1, 1), 5}",
                        "cube1", 1, 1, 1, 5
                },
                {
                    "a:{(3,-2,1.1),   3.3}  ",
                    "a", 3, -2, 1.1, 3.3
                },
                {
                    "cube3: {(-1.5, -1.6, 1), 8.8}",
                        "cube3", -1.5, -1.6, 1, 8.8
                },
                {
                        "cube4: {(0.5, -1.5, 1), 4}",
                        "cube4", 0.5, -1.5, 1, 4
                },
                {
                        "cube5: {(0.8, 2, -10), 4.5}",
                        "cube5", 0.8, 2, -10, 4.5
                },
                {
                        "cube6: {(1.4, 1, -1), 15.5}",
                        "cube6", 1.4, 1, -1, 15.5
                },
                {
                        "cube7: {(3582.99, -2857, 1.111111111),   3.6}  ",
                        "cube7", 3582.99, -2857, 1.111111111, 3.6
                },
                {
                        "cube8: {(-1.55, -186, 10.11), 345}",
                        "cube8", -1.55, -186, 10.11, 345
                },
                {
                        "cube9: {(0.85, 1555, -84.57), 71.1}",
                        "cube9", 0.85, 1555, -84.57, 71.1
                },
        };
    }

    @DataProvider
    public Object[] exceptionTestProvider() {
        return new Object[][]{
                {": {(1, 1.5, 1), -3.5}"},
                {"{(1, 2, 3), }"},
                {"{1.5, 2.5, 3.55 1}"},
                {"name: {(1.5, 3, -1), }"},
                {"name: (1, 3, 5), 5"},
                {"1.1, 3.5, 2.5, -2"},
                {"name: {(1, -2, 3), 0}"},
                {"name: {(1, 2, 3), 0}"},
                {"NAME: {(1, -1.5, 2), -5}"}
        };
    }

    @BeforeClass
    public void init() {
        factory = new CubeFactory();
    }

    @Test(dataProvider = "generalTestProvider")
    public void generalTest(
            String data,
            String name,
            double x, double y, double z,
            double edgeLength)
            throws GeometryException {
        Cube actual = factory.createFigure(data);
        Assert.assertTrue(TestComparator.compareCube(actual, name, x, y, z, edgeLength));
    }

    @Test(dataProvider = "exceptionTestProvider", expectedExceptions = {GeometryException.class})
    public void exceptionTest(String data) throws GeometryException{
        Cube cube = factory.createFigure(data);
    }


}
