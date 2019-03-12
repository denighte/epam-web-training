package by.radchuk.task2.context;

import by.radchuk.task2.entity.CurrencyType;

import java.util.HashMap;
import java.util.Map;

/**
 * CurrencyType table.
 * Contains information about exchange rates.
 */
final class ExchangeRateTable {
    /**
     * Singleton holder.
     */
    public static class SingletonHolder {
        /**
         * Singleton holder.
         */
        public static final ExchangeRateTable HOLDER_INSTANCE = new ExchangeRateTable();
    }

    /**
     * Singleton.
     * @return Singleton instance.
     */
    public static ExchangeRateTable getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }
    /**
     * currency adjacency map.
     */
    private static final Map<CurrencyType, Map<CurrencyType, Double>> MAP
            = new HashMap<>();

    /**
     * currency adjacency map getter.
     * @return currency adjacency map.
     */
    public Map<CurrencyType, Map<CurrencyType, Double>> getTable() {
        return MAP;
    }

    /**
     * adjacency map constructor.
     */
    private ExchangeRateTable() {
        //TODO: init map
    }

}
