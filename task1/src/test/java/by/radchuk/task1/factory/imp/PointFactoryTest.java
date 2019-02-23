package by.radchuk.task1.factory.imp;

import by.radchuk.task1.entity.Point;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.PointFactory;
import by.radchuk.task1.util.TestComparator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test class for ObservablePointFactory
 */
public class PointFactoryTest {
    private FigureFactory<Point> factory;

    @DataProvider
    public Object[][] generalTestProvider() {
        return new Object[][]{
                {"point1: (1, 1, 1)", "point1", 1, 1, 1},
                {"a:(3,2,1)  ", "a", 3, 2, 1},
                {"point3: (-1.5, -1.6, 1)", "point3", -1.5, -1.6, 1}
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
    public void generalTest(String data, String name, double x, double y, double z)
            throws GeometryException {
        Point actual = factory.createFigure(data);
        Assert.assertTrue(TestComparator.comparePoint(actual, name, x, y, z));
    }

    @Test(dataProvider = "exceptionTestProvider",
            expectedExceptions = {GeometryException.class})
    public void exceptionTest(String data) throws GeometryException {
        Point point = factory.createFigure(data);
        Assert.fail();
    }


}
