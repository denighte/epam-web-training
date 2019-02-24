package by.radchuk.task1.validator.impl;

import by.radchuk.task1.entity.Cube;
import by.radchuk.task1.entity.Point;
import by.radchuk.task1.exception.GeometryException;
import by.radchuk.task1.validator.FigureValidator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CubeValidatorTest {
    private FigureValidator<Cube> validator;

    @BeforeClass
    public void setUp() {
        validator = new CubeValidator();
    }

    @DataProvider
    public Object[][] testProvider() {
        return new Object[][] {
                { new Cube("Cube1", new Point("Cube1", 1, 1, 1){}, 0){} },
                { new Cube("Cube2", new Point("Cube2", 1, 1, 1){}, -5){} }
        };
    }

    @Test(dataProvider = "testProvider")
    public void validateFalseTest(Cube cube) {
        Assert.assertFalse(validator.validate(cube));
    }

    @Test
    public void validateTrueTest() {
        Cube cube = new Cube("Cube1", new Point("Cube1", 1, 1, 1){}, 2){};
        Assert.assertTrue(validator.validate(cube));
    }
}
