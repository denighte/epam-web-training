package by.radchuk.task4.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Html ResponseMessage class.
 * contains information which displayed by js on html page.
 */
@Data
@NoArgsConstructor
public class ResponseMessage {
    /**
     * Response status type.
     * (error, ok, ect.)
     */
    ResponseStatus status;
    /**
     * key to message to be displayed.
     * message will load from locale files.
     * leave null, if don't won't to display anything.
     */
    String messageKey;
    /**
     * data to be displayed.
     * Use to display not localizable messages.
     * leave null, if don't won't to display anything.
     */
    String data;
}
