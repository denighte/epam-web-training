package by.radchuk.task3.validator;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Validates matched text on continuity.
 */
@Slf4j
@AllArgsConstructor
public class TextContinuityValidator {
    /**
     * previous regex group end.
     */
    private int previousEnd;

    /**
     * Validates text on continuity.
     * @param currentStart current regex group start.
     * @param currentEnd current regex group end.
     * @return true, if continuous, otherwise false.
     */
    public boolean validate(final int currentStart,
                            final int currentEnd) {
        if (currentStart == previousEnd) {
            previousEnd = currentEnd;
            return true;
        }
        return false;
    }
}
