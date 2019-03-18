package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Sentence parser.
 */
@Slf4j
public class SentenceParser implements AbstractParser {
    /**
     * next parser in the chain.
     */
    private static final AbstractParser NEXT_PARSER = new LexemeParser();
    /**
     * punctuation mark parser.
     */
    private static final AbstractParser PUNCTUATION_MARK_PARSER
                                           = new PunctuationMarkParser();
    /**
     * sentence regex pattern.
     */
    private static final Pattern SENTENCE_PATTERN
            = Pattern.compile("([A-Z].*?)(\\.{3}|\\.|\\?|!)");

    /**
     * parse sentence from a string with data.
     * @param data TextElement data.
     * @return Sentence instance as TextElement.
     * @throws TextException in case parse error.
     */
    @Override
    public TextElement parse(final String data) throws TextException {
        log.info("Parsing sentence ...");
        List<TextElement> childrenElements = new ArrayList<>();

        for (String childElement : data.split("\\s+")) {
            childrenElements.add(NEXT_PARSER.parse(childElement));
        }

        log.info("Sentence parsed, creating sentence instance ...");
        return new TextElement(TextElementType.SENTENCE, childrenElements) {
            @Override
            public String toString() {
                return childElements
                        .stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(" "));
            }
        };
    }
}
