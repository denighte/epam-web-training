package by.radchuk.task2.context;

import by.radchuk.task2.entity.CurrencyType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * CurrencyType table.
 * Contains information about exchange rates.
 */
@AllArgsConstructor
public final class ExchangeRateTable {
    /**
     * order of currencies in the table.
     */
    @Getter
    private final String[] currencyOrder;
    /**
     * currency adjacency map.
     */
    private final BigDecimal[][] table;

    /**
     * get exchange rate value of two currency types.
     * @param type type to get.
     * @param toType type to convert.
     * @return exchange rate.
     */
    public BigDecimal getRate(final CurrencyType type,
                              final CurrencyType toType) {
        for (int i = 0; i < currencyOrder.length; ++i) {
            if (currencyOrder[i].equals(type.toString())) {
                for (int j = 0; j < currencyOrder.length; ++j) {
                    if (currencyOrder[j].equals(toType.toString())) {
                        return table[i][j];
                    }
                }
            }
        }
        return null;
    }

}
