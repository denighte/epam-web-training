package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import by.radchuk.task3.validator.TextContinuityValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Paragraph parser.
 */
@Slf4j
class ParagraphParser implements AbstractParser {
    /**
     * next parser in the chain.
     */
    private static final AbstractParser NEXT_PARSER = new SentenceParser();
    /**
     * Sentence pattern.
     */
    private static final Pattern SENTENCE_PATTERN
            = Pattern.compile("\\s*([A-Z].*?(?:\\.|\\.{3}|\\?|!))");

    /**
     * parse paragraph from a string with data.
     * @param data TextElement data.
     * @return paragraph instance as TextElement.
     * @throws TextException in case parse error.
     */
    @Override
    public TextElement parse(final String data) throws TextException {
        log.info("Parsing paragraph ...");
        List<TextElement> childrenElements = new ArrayList<>();

        Matcher matcher = SENTENCE_PATTERN.matcher(data);
        TextContinuityValidator validator = new TextContinuityValidator(0);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (!validator.validate(start, end)) {
                log.error("sentence data can't be parsed!");
                throw new TextException("invalid format of sentence data!");
            }
            childrenElements.add(NEXT_PARSER.parse(matcher.group(1)));
        }
        log.info("Paragraph parsed, creating paragraph instance ...");
        return new TextElement(TextElementType.PARAGRAPH, childrenElements) {
            @Override
            public String toString() {
                //Javac will make this StringBuilder.
                return "\t" + super.toString() + "\n";
            }
        };
    }
}
