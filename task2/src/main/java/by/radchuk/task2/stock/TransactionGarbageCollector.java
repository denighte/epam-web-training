package by.radchuk.task2.stock;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

@AllArgsConstructor
@Slf4j
public class TransactionGarbageCollector implements Runnable{
    private static final int COLLECT_TIMEOUT = 3000; //ms
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
            //bidders.removeIf(bidder -> bidder != null && bidder.getTransaction().isDone());
        }
    }
}
