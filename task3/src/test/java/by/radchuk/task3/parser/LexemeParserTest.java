package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.parser.AbstractParser;
import by.radchuk.task3.parser.LexemeParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LexemeParserTest {
    private AbstractParser parser;
    @BeforeClass
    void setUp() {
        parser = new LexemeParser();
    }

    @DataProvider
    Object [][] generalTestProvider() {
        return new Object[][] {
                {"remaining", "remaining"},
                {"5|(1&2&(3|(4&(25^5|6&47)|3)|2)|1)", "5"},
                {"English.", "English."},
                {"(8^5|1&2<<(2|5>>2&71))|1200", "1213"}
        };
    }

    @Test(dataProvider = "generalTestProvider")
    void generalTest(String data, String expected) throws TextException {
        TextElement lexeme = parser.parse(data);
        Assert.assertEquals(lexeme.toString(), expected);
    }
}
