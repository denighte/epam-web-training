package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LexemeParser implements AbstractParser {
    private static final AbstractParser WORD_PARSER = new WordParser();
    private static final AbstractParser EXPRESSION_PARSER = new ExpressionParser();
    private static final AbstractParser PUNCTUATION_PARSER = new PunctuationMarkParser();
    private static final Pattern LEXEME_PATTERN = Pattern.compile("(.*?)(\\.{3}|\\.|\\?|!|,|:|;)?");
    @Override
    public TextElement parse(String data) throws TextException {
        log.info("Parsing lexeme ...");
        List<TextElement> childrenElements = new ArrayList<>();
        Matcher matcher = LEXEME_PATTERN.matcher(data);
        if(!matcher.matches()) {
            log.error("Can't parse lexeme with data = {}", data);
            throw new TextException("Invalid format of lexeme!");
        }
        try {
            childrenElements.add(WORD_PARSER.parse(matcher.group(1)));
        } catch (TextException exception) {
            childrenElements.add(EXPRESSION_PARSER.parse(matcher.group(1)));
        }
        if(matcher.group(2) != null) {
            childrenElements.add(PUNCTUATION_PARSER.parse(matcher.group(2)));
        }
        log.info("Lexeme parsed, creating lexeme instance ...");
        return new TextElement(TextElementType.LEXEME, childrenElements);
    }
}
