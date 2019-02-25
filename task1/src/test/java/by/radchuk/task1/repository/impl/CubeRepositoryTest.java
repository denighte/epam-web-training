package by.radchuk.task1.repository.impl;

import by.radchuk.task1.action.CubeMath;
import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.CubeParameters;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.RepositoryCubeFactory;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class CubeRepositoryTest {
    private CubeRepository repository;
    private FigureFactory<Cube> factory;
    private List<Cube> cubes;

    @BeforeClass
    void init() {
        repository = CubeRepository.getInstance();
        factory = new RepositoryCubeFactory(repository);
        cubes = new ArrayList<>();
    }

    @DataProvider
    Object[][] testProvider() {
        return new Object[][] {
                {
                        "cube1: {(1, 1, 1), 5}"
                },
                {
                        "a:{(3, -2, 1.1), 3.3}  "
                },
                {
                        "cube3: {(-1.5, -1.6, 1), 8.8}"
                },
                {
                        "cube4: {(0.5, -1.5, 1), 4}"
                },
                {
                        "cube5: {(0.8, 2, -10), 4.5}"
                },
                {
                        "cube6: {(1.4, 1, -1), 15.5}"
                },
                {
                        "cube7: {(3582.99, -2857, 1.111111111), 3.6}"
                },
                {
                        "cube8: {(-1.55, -186, 10.11), 345}"
                },
                {
                        "cube9: {(0.85, 1555, -84.57), 71.1}"
                },
        };
    }

    @Test(dataProvider = "testProvider")
    void addTest(String data) throws GeometryException {
        Cube actual = factory.createFigure(data);
        cubes.add(actual);
        Assert.assertEquals(actual, repository.find(actual::equals).get(0));
    }

    @Test(dependsOnMethods = {"addTest"})
    void findByIdTest() {
        for (Cube cube : cubes) {
            Assert.assertEquals(cube, repository.find(cube.getId()));
        }
    }

    @Test(dependsOnMethods = {"findByIdTest"})
    void findTest() {
        List<Cube> expected = new ArrayList<>();
        for (Cube cube : cubes) {
            if(cube.getEdgeLength() > 10.5) {
                expected.add(cube);
            }
        }
        List<Cube> actual = repository.find(cube -> cube.getEdgeLength() > 10.5);
        Assert.assertEqualsNoOrder(actual.toArray(), expected.toArray());
    }

    @Test(dependsOnMethods = {"findTest"})
    void findByParametersTest() {
        CubeMath math = new CubeMath();
        List<Cube> expected = new ArrayList<>();
        for (Cube cube : cubes) {
            if (math.surfaceArea(cube) > 150) {
                expected.add(cube);
            }
        }
        List<Cube> actual = repository
                .findByParameters(parameters -> parameters.getSurfaceArea() > 150);
        Assert.assertEqualsNoOrder(actual.toArray(), expected.toArray());
    }

    @Test(dependsOnMethods = {"findByParametersTest"})
    void sortTest() {
        cubes.sort(Comparator.comparing(Cube::getEdgeLength));
        List<Cube> actual = repository.sort(Comparator.comparing(Cube::getEdgeLength));
        Assert.assertEquals(actual, cubes);
    }

    @Test(dependsOnMethods = {"sortTest"})
    void sortByParametersTest() {
        CubeMath math = new CubeMath();
        cubes.sort(Comparator.comparing(math::surfaceArea));
        List<Cube> actual = repository
                .sortByParameters(Comparator.comparing(CubeParameters::getSurfaceArea));
        Assert.assertEqualsNoOrder(actual.toArray(), cubes.toArray());
    }

    @Test(dependsOnMethods = {"sortByParametersTest"})
    void removeTest() {
        cubes.removeIf(cube -> cube.getCenterPoint().getX() < 0);
        repository.remove(cube -> cube.getCenterPoint().getX() < 0);
        List<Cube> actual = repository.find(cube -> true);
        Assert.assertEqualsNoOrder(actual.toArray(), cubes.toArray());
    }

    @Test(dependsOnMethods = {"removeTest"})
    void removeByParametersTest() {
        CubeMath math = new CubeMath();
        cubes.removeIf(cube -> math.surfaceArea(cube) > 150);
        repository.removeByParameters(parameters -> parameters.getSurfaceArea() > 150);
        List<Cube> actual = repository.find(cube -> true);
        Assert.assertEqualsNoOrder(actual.toArray(), cubes.toArray());
    }

    @Test(dependsOnMethods = {"removeByParametersTest"})
    void removeByIdTest() {
        for (Cube cube : cubes) {
            repository.remove(cube.getId());
            Assert.assertNull(repository.find(cube.getId()));
        }
    }

    @Test(dependsOnMethods = {"removeByIdTest"})
    void observableCubeTest() throws GeometryException{
        Cube expected = factory.createFigure("cube0: {(0, 0, 0), 2}");
        expected.setEdgeLength(3);
        Cube actual = repository.findByParameters(
                parameters ->
                        Double.compare(parameters.getSurfaceArea(), 54) == 0
                        && Double.compare(parameters.getVolume(), 27) == 0
                ).get(0);
        repository.remove(actual.getId());
        Assert.assertEquals(actual, expected);
    }

    @Test(dependsOnMethods = {"observableCubeTest"})
    void observablePointTest() throws GeometryException {
        CubeMath math = new CubeMath();
        Cube expected = factory.createFigure("cube0: {(2, 0, 0), 3}");
        expected.setEdgeLength(4);
        Cube actual = repository
                .findByParameters(CubeParameters::isOnCoordinatePlane)
                .get(0);
        repository.remove(actual.getId());
        Assert.assertEquals(actual, expected);
    }

}
