package by.radchuk.task2.context;

import by.radchuk.task2.entity.CurrencyType;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * CurrencyType table.
 * Contains information about exchange rates.
 */
@AllArgsConstructor
public final class ExchangeRateTable {
    /**
     * order of currencies in the table.
     */
    private final String[] currencyOrder;
    /**
     * currency adjacency map.
     */
    private final BigDecimal[][] table;
    /**
     * currency adjacency map getter.
     * @return currency adjacency map.
     */
    public BigDecimal getRate(CurrencyType type, CurrencyType toType) {
        for(int i = 0; i < currencyOrder.length; ++i) {
            if (currencyOrder[i].equals(type.toString())) {
                for(int j = 0; j < currencyOrder.length; ++j) {
                    if (currencyOrder[j].equals(toType.toString())) {
                        return table[i][j];
                    }
                }
            }
        }
        return null;
    }

}
