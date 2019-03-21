package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Punctuation mark parser.
 */
class PunctuationMarkParser implements AbstractParser {
    /**
     * next parser in the chain.
     */
    private static final AbstractParser NEXT_PARSER = new CharParser();
    /**
     * punctuation mark regex pattern.
     */
    private static final Pattern PUNCTUATION_MARK_PATTERN
                = Pattern.compile("(\\.{3}|\\.|\\?|!)");
    /**
     * parse punctuation mark.
     * @param data TextElement data.
     * @return Punctuation mark as TextElement.
     * @throws TextException in case parse error.
     */
    @Override
    public TextElement parse(final String data) throws TextException {
        List<TextElement> childrenElements = new ArrayList<>();
        for (int i = 0; i < data.length(); ++i) {
            childrenElements
                    .add(NEXT_PARSER.parse(Character.toString(data.charAt(i))));
        }
        return new TextElement(TextElementType.PUNCTUATION_MARK,
                               childrenElements);
    }
}
