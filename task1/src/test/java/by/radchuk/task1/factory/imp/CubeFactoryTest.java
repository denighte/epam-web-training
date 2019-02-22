package by.radchuk.task1.factory.imp;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.Point;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.CubeFactory;
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
                    new Cube("cube1",
                            new Point("cube1",1, 1, 1){}, 5){}
                },
                {
                    "a:{(3,-2,1.1),   3.3}  ",
                    new Cube("a",
                            new Point("a", 3, -2, 1.1){}, 3.3){}
                },
                {
                    "cube3: {(-1.5, -1.6, 1), 8.8}",
                    new Cube("cube3",
                            new Point("cube3", -1.5, -1.6, 1){}, 8.8){}
                },
                {
                        "cube4: {(0.5, -1.5, 1), 4}",
                        new Cube("cube4",
                                new Point("cube4", 0.5, -1.5, 1){}, 4){}
                },
                {
                        "cube5: {(0.8, 2, -10), 4.5}",
                        new Cube("cube5",
                                new Point("cube5", 0.8, 2, -10){}, 4.5){}
                }
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
    public void generalTest(String data, Cube expected) throws GeometryException{
        Cube actual = factory.createFigure(data);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "exceptionTestProvider", expectedExceptions = {GeometryException.class})
    public void exceptionTest(String data) throws GeometryException{
        Cube cube = factory.createFigure(data);
        Assert.fail();
    }


}
