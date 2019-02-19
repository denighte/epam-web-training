package by.radchuk.task1;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.factory.FigureFactory;
import by.radchuk.task1.factory.impl.CubeFactory;
import by.radchuk.task1.factory.impl.RepositoryCubeFactory;
import by.radchuk.task1.repository.impl.CubeRepository;
import org.testng.annotations.Test;

public class MainTest {
    @Test
    void test() {
        FigureFactory<Cube> factory1 = new RepositoryCubeFactory(CubeRepository.getInstance());
        Cube cube1 = factory1.createFigure("name: {(1, 1, 1), 1}");
        FigureFactory<Cube> factory2 = new CubeFactory();
        Cube cube2 = factory2.createFigure("name: {(1, 1, 1), 1}");

        System.out.println(cube1.equals(cube2));
        System.out.println("check");
    }
}