package by.radchuk.task3.action;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import by.radchuk.task3.parser.TextParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TextProcessorTest {
    private TextProcessor processor;
    private TextParser parser;
    @BeforeClass
    void setUp() {
        processor = new TextProcessor();
        parser = new TextParser();
    }

    @DataProvider
    Object [][] generalTestProvider() {
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
                        + "\tBye."}
        };
    }

    @DataProvider
    Object [][] sortBySentenceNumberTestProvider() {
        return new Object[][]{
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
                 "\tBye.\n"
                 + "\tIt is a 1213 established fact that a reader will be of a page when looking at its layout.\n"
                 + "\tIt has survived - not only (five) centuries, but also the leap into 52 electronic typesetting, "
                 + "remaining 3 essentially 9 unchanged.It was popularised in the 5 with the release of Letraset "
                 + "sheets containing Lorem Ipsum passages, and more recently with desktop publishing software "
                 + "like Aldus PageMaker including versions of Lorem Ipsum.\n"
                 + "\tIt is a long established fact that a reader will be distracted by the readable content "
                 + "of a page when looking at its layout.The point of using distribution of letters, "
                 + "as opposed to using (Content here), content here', making it look like readable English.\n" }
        };
    }

    @DataProvider
    Object[][] sortByWordLengthTestProvider() {
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
                "\t- 3 9 It 52 has not but the only also leap into (five) survived remaining centuries, "
                        + "electronic unchanged. essentially typesetting,"
                        + "5 It in of of was the the and with more with like Lorem Ipsum Aldus Lorem sheets Ipsum. "
                        + "release desktop Letraset recently software versions passages, "
                        + "PageMaker including containing publishing popularised\n"
                        + "\ta a a It is be by of at the its long fact that will page when "
                        + "reader content looking layout. readable distracted establishedof of as to it "
                        + "The look like point using using here), here', making opposed content letters, "
                        + "(Content readable English. distribution\n"
                        + "\ta a a It is be of at its 1213 fact that will page "
                        + "when reader looking layout. established\n"
                        + "\tBye.\n"}
        };
    }

    @DataProvider
    Object[][] sortLexemeBySymbolNumberTestProvider() {
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
                        new String[] {"release", "centuries,", "electronic", "typesetting,",
                                "essentially", "Letraset", "sheets", "recently", "PageMaker",
                                "established", "reader", "readable", "letters,", "here),",
                                "here',", "readable", "established", "reader", "survived",
                                "(five)", "the", "leap", "remaining", "unchanged.", "popularised",
                                "the", "the", "Lorem", "passages,", "more", "desktop", "software",
                                "like", "versions", "Lorem", "be", "distracted", "the", "content",
                                "page", "when", "The", "opposed", "(Content", "content", "like",
                                "be", "page", "when", "Bye.", "It", "has", "-", "not", "only",
                                "but", "also", "into", "52", "3", "9", "It", "was", "in", "5",
                                "with", "of", "containing", "Ipsum", "and", "with", "publishing",
                                "Aldus", "including", "of", "Ipsum.", "It", "is", "a", "long",
                                "fact", "that", "a", "will", "by", "of", "a", "looking", "at",
                                "its", "layout.", "point", "of", "using", "distribution", "of",
                                "as", "to", "using", "making", "it", "look", "English.", "It", "is",
                                "a", "1213", "fact", "that", "a", "will", "of", "a", "looking",
                                "at", "its", "layout.", }}
        };
    }

    @Test(dataProvider = "sortBySentenceNumberTestProvider")
    void sortBySentenceNumberTest(String data, String expected) throws TextException {
        TextElement text = parser.parse(data);
        processor.containerSort(text, TextElementType.PARAGRAPH, (lhs, rhs) -> {
            int result = lhs.getChildElements().size() - rhs.getChildElements().size();
            if (result == 0) {
                return lhs.toString().compareTo(rhs.toString());
            }
            return result;
            });
        Assert.assertEquals(text.toString(), expected);
    }

    @Test(dataProvider = "sortByWordLengthTestProvider")
    void sortByWordLengthTest(String data, String expected) throws TextException {
        TextElement text = parser.parse(data);
        processor.containerSort(text,
                       TextElementType.LEXEME,
                       Comparator.comparingInt(obj -> obj.toString().length()));
        Assert.assertEquals(text.toString(), expected);
    }

    @Test(dataProvider = "sortLexemeBySymbolNumberTestProvider")
    void sortLexemeBySymbolNumberTest(String data, String[] expected) throws TextException {
        TextElement text = parser.parse(data);
        char charToCount = 'e';
        List<TextElement> sortedText = processor.deepSort(text,
                       TextElementType.LEXEME,
                       (lhs, rhs) -> {
                            return (int)(rhs.toString().chars().filter(ch -> ch == charToCount).count()
                                    - lhs.toString().chars().filter(ch -> ch == charToCount).count());
                       });
        List<String> actual = sortedText.stream().map(textElement -> textElement.toString())
                                .collect(Collectors.toList());
        Assert.assertEquals(actual, Arrays.asList(expected));
    }
}
