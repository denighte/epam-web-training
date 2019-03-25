package by.radchuk.task3.expression;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ExpressionCompilerTest {
    @DataProvider
    Object[][] generalTestProvider() {
        return new Object[][] {
                {"13<<2", 52},
                {"30>>>3", 3},
                {"~6&9|(3&4)", 9},
                {"5|(1&2&(3|(4&(25^5|6&47)|3)|2)|1)", 5},
                {"(8^5|1&2<<(2|5>>2&71))|1200", 1213}
        };
    }

    @Test(dataProvider = "generalTestProvider")
    void test(String expression, long expected) {
        Assert.assertEquals(ExpressionCompiler.compile(expression).execute(), expected);
    }
}
