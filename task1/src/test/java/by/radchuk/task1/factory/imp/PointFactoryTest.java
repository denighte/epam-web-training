package by.radchuk.task1.factory.imp;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import by.radchuk.task1.entity.Point;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.PointFactory;

/**
 * Test class for ObservablePointFactory
 */
public class PointFactoryTest {
    private FigureFactory<Point> factory;

    @DataProvider
    public Object[][] generalTestProvider() {
        return new Object[][]{
                {"point1: (1, 1, 1)", new Point("point1",1, 1, 1)},
                {"a:(3,2,1)  ", new Point("a", 3, 2, 1)},
                {"point3: (-1.5, -1.6, 1)", new Point("point3", -1.5, -1.6, 1)}
        };
    }

    @DataProvider
    public Object[] exceptionTestProvider() {
        return new Object[][]{
                {": (1, 1.5, 1)"},
                {"(1, 2, 3)"},
                {"(1.5, 2.5, 3.55"},
                {"name: (1.5, 3)"},
                {"name: 1, 3, 5"},
                {"1.1, 3.5, 2.5"}
        };
    }

    @BeforeClass
    public void init() {
        factory = new PointFactory();
    }

    @Test(dataProvider = "generalTestProvider")
    public void generalTest(String data, Point expected) {
        Point actual = factory.createFigure(data);
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "exceptionTestProvider", expectedExceptions = {GeometryException.class})
    public void exceptionTest(String data) {
        Point point = factory.createFigure(data);
        Assert.fail();
    }
}
