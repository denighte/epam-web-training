package by.radchuk.task3.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TextElementType {
    /**
     * TextElement type.
     */
    TEXT("TEXT"),
    /**
     * Paragraph type.
     */
    PARAGRAPH("PARAGRAPH"),
    /**
     * Sentence type.
     */
    SENTENCE("SENTENCE"),
    /**
     * Lexeme type.
     */
    LEXEME("LEXEME"),
    /**
     * Word type.
     */
    WORD("WORD"),
    /**
     * Expression type.
     */
    EXPRESSION("EXPRESSION"),
    /**
     * Punctuation mark type
     */
    PUNCTUATION_MARK("PUNCTUATION MARK"),
    /**
     * None type.
     * type of the tree leaf.
     */
    NONE("NONE");

    /**
     * TextElement element type name;
     */
    private final String name;

    /**
     * String value of element type.
     * @return string representation of element type.
     */
    @Override
    public String toString() {
        return this.name;
    }
}
