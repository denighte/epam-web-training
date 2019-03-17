package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SentenceParserTest {
    AbstractParser sentenceParser;
    @BeforeClass
    void setUp() {
        sentenceParser = new SentenceParser();
    }

    @DataProvider
    Object[][] generalTestProvider() {
        return new Object[][] {
                {"It has survived - not only (five) centuries,"
                 + " but also the leap into 13<<2 electronic typesetting,"
                 + " remaining 30>>>3 essentially ~6&9|(3&4) unchanged."},
                {"It was popularised in the 5|(1&2&(3|(4(&(25^5|6&47)|3)|2)|1)"
                 + "with the release of Letraset sheets containing Lorem Ipsum passges,"
                 + " and more recently with desktop publishing software "
                 + "like Aldus PageMaker including versions of Lorem Ipsum."}
        };
    }

    @Test(dataProvider = "generalTestProvider")
    void generalTest(String data) throws TextException {
        TextElement text = sentenceParser.parse(data);
        System.out.println(text);
        //Assert.assertEquals(text.toString(), data);
    }
}
