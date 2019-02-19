package by.radchuk.task1.observer;

/**
 * observable interface.
 * notifies observer every time when object changes.
 */
public interface IObservable {
    /**
     * called when observer has to be notified.
     */
    void notifyObserver();
}
