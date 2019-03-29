package by.radchuk.task4.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Response status enum.
 * The output data of servlet will differ
 * depending on the response status.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResponseStatus {
    /**
     * OK status.
     */
    OK("OK"),
    /**
     * ERROR status.
     */
    ERROR("ERROR"),
    /**
     * TABLE status.
     * Used when information should be displayed in table.
     */
    TABLE("TABLE");

    /**
     * status string representation.
     */
    private String name;

    /**
     * returns string representation of the status.
     * @return status string representation.
     */
    @Override
    public String toString() {
        return name;
    }

}
