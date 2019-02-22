package by.radchuk.task1.repository.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.imp.CubeFactoryTest;
import by.radchuk.task1.factory.impl.RepositoryCubeFactory;
import by.radchuk.task1.repository.FigureRepository;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.*;

@Slf4j
public class CubeRepositoryTest {
    private FigureRepository<Cube> repository;
    private FigureFactory<Cube> factory;
    @BeforeClass
    public void init() {
        repository = CubeRepository.getInstance();
        factory = new RepositoryCubeFactory(repository);
    }

    @Test(dataProvider = "generalTestProvider", dataProviderClass = CubeFactoryTest.class)
    void addTest(String data, Cube ignore) throws GeometryException {
        Cube actual = factory.createFigure(data);
        Assert.assertEquals(actual, repository.find(actual::equals));
    }

    private boolean equalCubes(Cube lhs, Cube rhs) {
        return lhs.getEdgeLength() == rhs.getEdgeLength()
                && lhs.getCenterPoint().getX() == rhs.getCenterPoint().getX()
                && lhs.getCenterPoint().getY() == rhs.getCenterPoint().getY()
                && lhs.getCenterPoint().getZ() == rhs.getCenterPoint().getZ();
    }

}
