package by.radchuk.task2.stock;

import by.radchuk.task2.entity.Currency;

import java.util.HashMap;
import java.util.Map;

/**
 * Currency table.
 * Contains information about exchange rates.
 */
final class CurrencyTable {
    /**
     * Singleton holder.
     */
    public static class SingletonHolder {
        /**
         * Singleton holder.
         */
        public static final CurrencyTable HOLDER_INSTANCE = new CurrencyTable();
    }

    /**
     * Singleton.
     * @return Singleton instance.
     */
    public static CurrencyTable getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }
    /**
     * currency adjacency map.
     */
    private static final Map<Currency, Map<Currency, Double>> MAP
            = new HashMap<>();
    /**
     * adjacency map constructor.
     */
    private CurrencyTable() {
        //TODO: init map
    }

}
