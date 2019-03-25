package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class WordParserTest {
    private AbstractParser parser;
    @BeforeClass
    void setUp() {
        parser = new WordParser();
    }

    @DataProvider
    Object[][] generalTestProvider() {
        return new Object[][] {
                {"Word", "Word"},
                {"abc", "abc"},
                {"def", "def"}
        };
    }

    @DataProvider
    Object[][] exceptionTestProvider() {
        return new Object[][] {
                {"Word."},
                {"acd..."},
                {" de "}
        };
    }

    @Test(dataProvider = "generalTestProvider")
    void generalTest(String data, String expected) throws TextException {
        TextElement word = parser.parse(data);
        Assert.assertEquals(word.toString(), expected);
    }

    @Test(dataProvider = "exceptionTestProvider",expectedExceptions = {TextException.class})
    void exceptionTest(String data) throws TextException {
        parser.parse(data);
        Assert.fail();
    }
}
