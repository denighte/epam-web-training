package by.radchuk.task2.stock;

import lombok.AllArgsConstructor;

import java.util.Queue;

@AllArgsConstructor
public class TransactionGarbageCollector implements Runnable{
    private static final int COLLECT_TIMEOUT = 10000; //ms
    private Queue<Bidder> bidders;


    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(COLLECT_TIMEOUT);
            } catch (InterruptedException ignore) {
                return;
            }
            bidders.removeIf(bidder -> bidder.getTransaction().isDone());
        }
    }
}
