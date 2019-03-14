package by.radchuk.task3.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates matched text on continuity.
 */
@Slf4j
@AllArgsConstructor
public class TextMatchValidator {
    /**
     * previous regex group end.
     */
    int previousEnd;

    /**
     * Validates text on continuity.
     * @param currentStart current regex group start.
     * @param currentEnd current regex group end.
     * @return true, if continuous, otherwise false.
     */
    public boolean validate(int currentStart, int currentEnd) {
        if (currentStart == previousEnd) {
            previousEnd = currentEnd;
            return true;
        }
        return false;
    }
}
