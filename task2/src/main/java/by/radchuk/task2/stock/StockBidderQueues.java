package by.radchuk.task2.stock;

import by.radchuk.task2.context.EnvironmentContext;
import by.radchuk.task2.entity.CurrencyType;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * stock bidder currency to currency queues class.
 */
class StockBidderQueues {
    /**
     * currency to currency bidder queues.
     */
    private Queue<Bidder>[][] table;
    /**
     * order of currencies in the queues table.
     */
    private String[] currencyOrder;

    /**
     * Default constructor.
     * Gets number of currencies from EnvironmentContext.
     * @see by.radchuk.task2.context.EnvironmentContext
     */
    StockBidderQueues() {
        currencyOrder = EnvironmentContext
                .getInstance()
                .getExchangeRateTable()
                .getCurrencyOrder();
        table = (Queue<Bidder>[][])
                new Queue[currencyOrder.length][currencyOrder.length];
        for (int i = 0; i < currencyOrder.length; ++i) {
            for (int j = 0; j < currencyOrder.length; ++j) {
                table[i][j] = new ConcurrentLinkedQueue<>();
            }
        }
    }

    /**
     * get currency to currency bidders queue.
     * @param sell currency type to sell.
     * @param buy currency type to buy.
     * @return bidders queue.
     */
    Queue<Bidder> getQueue(final CurrencyType sell,
                                 final CurrencyType buy) {
        for (int i = 0; i < currencyOrder.length; ++i) {
            if (currencyOrder[i].equals(sell.toString())) {
                for (int j = 0; j < currencyOrder.length; ++j) {
                    if (currencyOrder[j].equals(buy.toString())) {
                        return table[i][j];
                    }
                }
            }
        }
        return null;
    }
}
