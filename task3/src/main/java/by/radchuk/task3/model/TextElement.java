package by.radchuk.task3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TextElement class.
 * Composite pattern.
 * Holds paragraphs array.
 */
@NoArgsConstructor
@AllArgsConstructor
public class TextElement {
    /**
     * Type of the element.
     */
    @Getter
    protected TextElementType type = TextElementType.NONE;
    /**
     * children elements list.
     */
    @Getter
    protected List<TextElement> childElements = Collections.emptyList();

    /**
     * Returns string representation of TextElement.
     * @return
     */
    @Override
    public String toString() {
        return childElements
                    .stream()
                    .map(Objects::toString)
                    .collect(Collectors.joining(""));
    }
}
