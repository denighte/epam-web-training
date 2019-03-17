package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
@Slf4j
public class LexemeParser implements AbstractParser {
    private static final AbstractParser WORD_PARSER = new WordParser();
    private static final AbstractParser EXPRESSION_PARSER = new ExpressionParser();
    @Override
    public TextElement parse(String data) throws TextException {
        log.info("Parsing lexeme ...");
        TextElement lexeme = null;
        try {
             lexeme = WORD_PARSER.parse(data);
        } catch (TextException exception) {
            lexeme = EXPRESSION_PARSER.parse(data);
        }
        log.info("Lexeme parsed, creating lexeme instance ...");
        return new TextElement(TextElementType.LEXEME, Arrays.asList(lexeme));
    }
}
