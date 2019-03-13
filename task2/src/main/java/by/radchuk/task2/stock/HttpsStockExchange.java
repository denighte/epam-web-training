package by.radchuk.task2.stock;

import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * stock exchange implementation.
 */
@Slf4j
public final class HttpsStockExchange {
    /**
     * threads number.
     */
    private static final int THREAD_NUMBER = 4;
    /**
     * Singleton.
     */
    private static final HttpsStockExchange INSTANCE = new HttpsStockExchange();
    /**
     * Singleton getInstance.
     * @return Singleton instance.
     */
    public static HttpsStockExchange getInstance() {
        return INSTANCE;
    }

    /**
     * Bidder queue.
     */
    private StockBidderQueues queues;
    /**
     * transactions thread pool.
     */
    @Getter
    private ExecutorService threadPool;

    /**
     * private constructor to prevent direct class creation.
     */
    private HttpsStockExchange() {
        log.info("\nCreating HttpsStockExchange.\n");
        queues = new StockBidderQueues();
        threadPool = Executors.newFixedThreadPool(THREAD_NUMBER);
        log.info("\nCreated HttpsStockExchange.\n");
    }

    /**
     * sell currency on stock.
     * @param user user participant.
     * @param currencyToSell currency to sell.
     * @param currencyTypeToBuy currency to buy.
     */
    public void sellCurrency(final User user,
                             final Currency currencyToSell,
                             final CurrencyType currencyTypeToBuy) {
        Bidder seller
                = new Bidder(user, currencyToSell, currencyTypeToBuy);
        Queue<Bidder> handler
                = queues.getQueue(currencyToSell.getType(), currencyTypeToBuy);
        handler.removeIf(buyer -> {
            if (
                buyer == null
                || (buyer.getTransaction() != null
                && buyer.getTransaction().isDone())
            ) {
                return true;
            } else if (buyer.getTransaction() == null) {
                startTransaction(seller, buyer);
                return false;
            }
            return false;
        });
        handler.add(seller);
    }

    /**
     * starts transaction.
     * @param seller currency seller
     * @param buyer currency buyer
     */
    private void startTransaction(
            final Bidder seller,
            final Bidder buyer) {
        Transaction transaction = new Transaction(
                seller.getUser(),
                buyer.getUser(),
                seller.getCurrencyToSell().getType(),
                buyer.getCurrencyTypeToBuy(),
                seller.getCurrencyToSell().getAmount()
        );
        Future<BigDecimal> future = threadPool.submit(transaction);
        buyer.setTransaction(future);
        seller.setTransaction(future);
    }
}
