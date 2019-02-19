package by.radchuk.task1.observer;

/**
 * observer interface.
 * gets notified every time when observable changes.
 */
public interface IObserver {
    /**
     * called every time an object changes.
     * @param id object id.
     */
    void update(int id);
}
