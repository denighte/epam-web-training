package by.radchuk.task3.action;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.parser.TextParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Task3Test {
    private Task3 processor;
    private TextParser parser;
    @BeforeClass
    void setUp() {
        processor = new Task3();
        parser = new TextParser();
    }

    @Test(dataProviderClass = TextProcessorTest.class, dataProvider = "sortBySentenceNumberTestProvider")
    void sortBySentenceNumberTest(String data, String expected) throws TextException {
        TextElement text = parser.parse(data);
        processor.sortBySentenceNumber(text);
        Assert.assertEquals(text.toString(), expected);
    }

    @Test(dataProviderClass = TextProcessorTest.class, dataProvider = "sortByWordLengthTestProvider")
    void sortByWordLengthTest(String data, String expected) throws TextException {
        TextElement text = parser.parse(data);
        processor.sortByWordLength(text);
        Assert.assertEquals(text.toString(), expected);
    }

    @Test(dataProviderClass = TextProcessorTest.class, dataProvider = "sortLexemeBySymbolNumberTestProvider")
    void sortLexemeBySymbolNumberTest(String data, String[] expected) throws TextException {
        TextElement text = parser.parse(data);
        char charToCount = 'e';
        List<TextElement> sortedText = processor.sortLexemeBySymbolNumber(text, 'e');
        List<String> actual = sortedText.stream().map(Objects::toString)
                .collect(Collectors.toList());
        Assert.assertEquals(actual, Arrays.asList(expected));
    }
}
