package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TextParserTest {

    private AbstractParser textParser;

    @BeforeClass
    void setUp() {
        textParser = new TextParser();
    }

    @DataProvider
    Object[][] generalTestProvider() {
        return new Object[][] {
                {"\tIt has survived - not only (five) centuries,"
                 + " but also the leap into 13<<2 electronic typesetting,"
                 + " remaining 30>>>3 essentially ~6&9|(3&4) unchanged."
                 + "It was popularised in the 5|(1&2&(3|(4&(25^5|6&47)|3)|2)|1)"
                 + " with the release of Letraset sheets containing Lorem Ipsum passages,"
                 + " and more recently with desktop publishing software "
                 + "like Aldus PageMaker including versions of Lorem Ipsum.\n"
                 + "\tIt is a long established fact that a reader will be distracted by"
                 + "   the readable content  of a   page when looking  at its layout."
                 + "   The  point of  using distribution of letters,"
                 + " as opposed to using (Content here), content here',"
                 + "  making it look like readable English.\n\n"
                 + "\tIt is a (8^5|1&2<<(2|5>>2&71))|1200 established "
                 + "fact that a reader will be of a page when looking at its layout.\n"
                 + "\tBye.",
                        "\tIt has survived - not only (five) centuries,"
                        + " but also the leap into 52 electronic typesetting,"
                        + " remaining 3 essentially 9 unchanged."
                        + "It was popularised in the 5"
                        + " with the release of Letraset sheets containing Lorem Ipsum passages,"
                        + " and more recently with desktop publishing software "
                        + "like Aldus PageMaker including versions of Lorem Ipsum.\n"
                        + "\tIt is a long established fact that a reader will be distracted by"
                        + " the readable content of a page when looking at its layout."
                        + "The point of using distribution of letters,"
                        + " as opposed to using (Content here), content here',"
                        + " making it look like readable English.\n"
                        + "\tIt is a 1213 established "
                        + "fact that a reader will be of a page when looking at its layout.\n"
                        + "\tBye.\n"},
                {"Bye.", "\tBye.\n"}
        };
    }

    @Test(dataProvider = "generalTestProvider")
    void generalTest(String data, String expected) throws TextException {
        TextElement text = textParser.parse(data);
        Assert.assertEquals(text.toString(), expected);
    }
}
