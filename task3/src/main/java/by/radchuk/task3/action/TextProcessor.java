package by.radchuk.task3.action;

import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Performs operations on text and text element classes.
 */
@Slf4j
public class TextProcessor {
    /**
     * sorts elements of specified type within the container.
     * @param text text or text element, to perform operation.
     * @param type text element type to sort.
     * @param cmp comparator, to compare text elements.
     */
    public void containerSort(final TextElement text,
                              final TextElementType type,
                              final Comparator<TextElement> cmp) {
        if (text.getChildElements().size() != 0
                && text.getChildElements().get(0).getType().equals(type)) {
            log.info("Sorting text element children with text type = {}",
                                                        text.getType());
            text.getChildElements().sort(cmp);
            return;
        }

        for (TextElement element : text.getChildElements()) {
            containerSort(element, type, cmp);
        }
    }

    /**
     * sorts elements of specified type and returns them as list.
     * @param text text or text element, to perform operation.
     * @param type text element type to sort.
     * @param cmp comparator, to compare text elements.
     * @return list of sorted text elements.
     */
    public List<TextElement> deepSort(final TextElement text,
                                      final TextElementType type,
                                      final Comparator<TextElement> cmp) {
        List<TextElement> result = new ArrayList<>();
        deepSortRecursion(text, type, result);
        result.sort(cmp);
        return result;
    }

    /**
     * deep sort recursion implementation.
     * @param text text or text element, to perform operation.
     * @param type text element or type to sort.
     * @param result list of text elements.
     */
    private void deepSortRecursion(final TextElement text,
                                   final TextElementType type,
                                   final List<TextElement> result) {
        if (text.getChildElements().size() != 0
                && text.getChildElements().get(0).getType().equals(type)) {
            result.addAll(text.getChildElements());
        }
        for (TextElement element : text.getChildElements()) {
            deepSortRecursion(element, type, result);
        }
    }
}
