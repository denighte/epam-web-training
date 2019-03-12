package by.radchuk.task2.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * CurrencyType constants.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CurrencyType {
    /**
     * United States Dollar currency.
     */
    USD("USD"),
    /**
     * Euro currency.
     */
    EUR("EUR"),
    /**
     * Belarusian ruble currency.
     */
    BYN("BYN");
    /**
     * currency name.
     */
    private final String name;

    @Override
    public String toString() {
        return this.name;
    }
}
