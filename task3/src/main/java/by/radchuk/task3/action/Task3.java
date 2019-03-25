package by.radchuk.task3.action;

import by.radchuk.task3.model.TextElement;
import by.radchuk.task3.model.TextElementType;

import java.util.Comparator;
import java.util.List;

/**
 * Performs operations defined in task 3.
 */
public class Task3 {
    /**
     * Text processor which performs operations.
     */
    private TextProcessor processor = new TextProcessor();

    /**
     * sort text by sentence number.
     * @param text text to sort.
     */
    void sortBySentenceNumber(final TextElement text) {
        processor.containerSort(text, TextElementType.PARAGRAPH, (lhs, rhs) -> {
            int result = lhs.getChildElements().size()
                        - rhs.getChildElements().size();
            if (result == 0) {
                return lhs.toString().compareTo(rhs.toString());
            }
            return result;
        });
    }

    /**
     * sort text by words length.
     * @param text text to sort.
     */
    void sortByWordLength(final TextElement text) {
        processor.containerSort(text,
                TextElementType.LEXEME,
                Comparator.comparingInt(obj -> obj.toString().length()));
    }

    /**
     * sort lexemes by symbol number.
     * @param text text to find lexemes.
     * @param symbol symbol to sort by.
     * @return list of sorted lexemes.
     */
    List<TextElement> sortLexemeBySymbolNumber(final TextElement text,
                                               final char symbol) {
        return processor.deepSort(text,
                TextElementType.LEXEME,
                (lhs, rhs) ->
                (int) (rhs.toString().chars().filter(ch -> ch == symbol).count()
                - lhs.toString().chars().filter(ch -> ch == symbol).count())
        );
    }
}
