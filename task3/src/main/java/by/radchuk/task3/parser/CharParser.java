package by.radchuk.task3.parser;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.Char;
import by.radchuk.task3.model.TextElement;

public class CharParser implements AbstractParser {
    @Override
    public TextElement parse(String data) throws TextException {
        return new Char(data.charAt(0));
    }
}
