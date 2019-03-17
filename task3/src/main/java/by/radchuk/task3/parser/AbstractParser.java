package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;

public interface AbstractParser {
    /**
     * Creates TextElement from string with data.
     * @param data TextElement data.
     * @return TextElement instance
     * @throws TextException in case parse error.
     */
    TextElement parse(String data) throws TextException;
}
