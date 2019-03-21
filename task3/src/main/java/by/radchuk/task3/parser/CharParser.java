package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.Char;
import by.radchuk.task3.model.TextElement;

/**
 * Char parser.
 * parser char from a string representation.
 */
class CharParser implements AbstractParser {
    /**
     * Parsers char from a string with data.
     * @param data Char data.
     * @return Char instance as TextElement.
     * @throws TextException in case parse error.
     */
    @Override
    public TextElement parse(final String data) throws TextException {
        return new Char(data.charAt(0));
    }
}
