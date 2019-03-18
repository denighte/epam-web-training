package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Word parser.
 */
@Slf4j
public class WordParser implements AbstractParser {
    /**
     * next parser in the chain.
     */
    private static final AbstractParser NEXT_PARSER = new CharParser();
    private static final Pattern WORD_PATTERN = Pattern.compile("\\(?[\\w'-]+\\)?");
    /**
     * parses word from string with data.
     * @param data TextElement data.
     * @return Word instance as TextElement.
     * @throws TextException in case parse error.
     */
    @Override
    public TextElement parse(final String data) throws TextException {
        List<TextElement> childrenElements = new ArrayList<>();

        Matcher matcher = WORD_PATTERN.matcher(data);
        if(!matcher.matches()) {
            log.warn("word can't be parsed! data={}", data);
            throw new TextException("invalid format of word!");
        }

        for(int i = 0; i < matcher.group(0).length(); ++i) {
            childrenElements.add(NEXT_PARSER.parse(Character.toString(data.charAt(i))));
        }

        return new TextElement(TextElementType.WORD, childrenElements);
    }
}
