package by.radchuk.task2.stock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

/**
 * collects the garbage in the queue.
 */
@AllArgsConstructor
@Slf4j
@Deprecated
public final class TransactionGarbageCollector implements Runnable {
    /**
     * collection await time.
     */
    private static final int COLLECT_TIMEOUT = 3000; //ms
    /**
     * queue to clear.
     */
    private Queue<Bidder> bidders;

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(COLLECT_TIMEOUT);
            } catch (InterruptedException ignore) {
                return;
            }
            System.out.println("\n\n\n\n\n\n");
            log.info("bidders queue size = {}", bidders.size());
            bidders.removeIf(bidder
                    -> bidder != null && bidder.getTransaction().isDone());
        }
    }
}
