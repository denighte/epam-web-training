package by.radchuk.task1.reader.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.impl.CubeFactory;
import by.radchuk.task1.reader.FigureReader;
import by.radchuk.task1.util.TestComparator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class CubeReaderTest {

    FigureReader<Cube> reader;
    List<Cube> result;
    int testNumber;

    @BeforeClass
    void setUp() {
        reader = new CubeReader(new CubeFactory());
    }

    @DataProvider
    Object[][] resultProvider() {
        return new Object[][]{
                {
                        "cube1", 1, 1, 1, 5
                },
                {
                        "a", 3, -2, 1.1, 3.3
                },
                {
                        "cube3", -1.5, -1.6, 1, 8.8
                },
                {
                        "cube4", 0.5, -1.5, 1, 4
                },
                {
                        "cube5", 0.8, 2, -10, 4.5
                },
                {
                        "cube6", 1.4, 1, -1, 15.5
                },
                {
                        "cube7", 3582.99, -2857, 1.111111111, 3.6
                },
                {
                        "cube8", -1.55, -186, 10.11, 345
                },
                {
                        "cube9", 0.85, 1555, -84.57, 71.1
                },
        };
    }

    @Test
    @Parameters({"readerTestFile"})
    void readTest(String path) throws IOException, GeometryException {
        result = reader.read(Paths.get(path));
    }

    @Test(dataProvider = "resultProvider")
    void readTestCheck(String name, double x, double y, double z, double edgeLength) {
        Assert.assertTrue(
                TestComparator.compareCube(result.get(testNumber++), name, x, y, z, edgeLength));
    }



}
