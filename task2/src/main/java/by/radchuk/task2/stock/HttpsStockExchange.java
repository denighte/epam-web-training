package by.radchuk.task2.stock;

import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

/**
 * stock exchange implementation.
 */
@Slf4j
final public class HttpsStockExchange {
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
     * mutex.
     */
    private Lock lock = new ReentrantLock();
    /**
     * Bidder queue.
     */
    private Queue<Bidder> bidderQueue;
    /**
     * Collects ended tasks.
     */
    //TransactionGarbageCollector garbageCollector;
    /**
     * transactions thread pool.
     */
    @Getter
    private ExecutorService threadPool;

    /**
     * private constructor to prevent direct class creation.
     */
    private HttpsStockExchange() {
        log.info("\nCreated HttpsStockExchange.\n");
        bidderQueue = new ConcurrentLinkedQueue<>();
        threadPool = Executors.newFixedThreadPool(THREAD_NUMBER);
        Thread collector
                = new Thread(new TransactionGarbageCollector(bidderQueue));
        collector.setDaemon(true);
        collector.start();
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
        Bidder futureBidder
                = new Bidder(user, currencyToSell, currencyTypeToBuy);
        for (Bidder bidder : bidderQueue) {
            if (futureBidder.getCurrencyToSell().getType()
                    .equals(bidder.getCurrencyTypeToBuy())) {
                if (bidder.getTransaction() == null) {
                    lock.lock();
                        if (bidder.getTransaction() == null) {
                            startTransaction(futureBidder, bidder);
                            bidderQueue.add(futureBidder);
                            lock.unlock();
                            return;
                        }
                    lock.unlock();
                }
            }
        }
        bidderQueue.add(futureBidder);
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
