package by.radchuk.task3.model;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class Expression extends TextElement {
    private int value;
    public Expression(List<TextElement> children, final int number) {
        super(TextElementType.EXPRESSION, children);
        value = number;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
