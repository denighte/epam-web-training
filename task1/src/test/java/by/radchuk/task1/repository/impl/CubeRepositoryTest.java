package by.radchuk.task1.repository.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.CubeFactoryTest;
import by.radchuk.task1.factory.impl.RepositoryCubeFactory;
import by.radchuk.task1.repository.FigureRepository;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
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
        for(Cube cube : cubes) {
            Assert.assertEquals(cube, repository.find(cube.getId()));
        }
    }

    @Test
    void findTest() {
        //TODO: add impl
    }


}
