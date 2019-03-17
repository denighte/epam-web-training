package by.radchuk.task3.model;

import java.util.Collections;
import java.util.List;

public class Char extends TextElement {
    /**
     * Char value.
     */
    private char aChar;

    /**
     * default constructor from char.
     * @param c char
     */
    public Char(char c) {
        aChar = c;
        type = TextElementType.CHAR;
        childElements = Collections.emptyList();
    }

    /**
     * returns string representation of the char.
     * @return string representation of the char.
     */
    @Override
    public String toString() {
        return Character.toString(aChar);
    }
}
