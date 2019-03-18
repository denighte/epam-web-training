package by.radchuk.task3.action;

import by.radchuk.task3.exception.TextException;
import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class TextProcessor {
    public void sort(final TextElement text,
                     final TextElementType type,
                     final Comparator<TextElement> comparator)
                                         throws TextException {
        if (text.getChildElements().size() != 0
                && text.getChildElements().get(0).getType().equals(type)) {
            log.info("Sorting text element children with text type = {}", text.getType());
            text.getChildElements().sort(comparator);
            return;
        }

        for (TextElement element : text.getChildElements()) {
            sort(element, type, comparator);
        }
    }

    public TextElement deepSort(final TextElement text,
                                final TextElementType type,
                                final Comparator<TextElement> comparator) {
        if (text.getChildElements().size() != 0
                && text.getChildElements().get(0).getType().equals(type)) {
            log.info("Sorting text element children with text type = {}", text.getType());
            text.getChildElements().sort(comparator);
        }
        List<TextElement> elements = new ArrayList<>(text.getChildElements());
        return new TextElement(text.getType(), elements);
    }
}
