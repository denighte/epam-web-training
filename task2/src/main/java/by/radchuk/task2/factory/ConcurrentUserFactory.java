package by.radchuk.task2.factory;

import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.User;
import by.radchuk.task2.exception.ExchangeException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * extends user for concurrent programming environment usage.
 */
public class ConcurrentUserFactory extends UserFactory {
    /**
     * creates User instance from a string.
     * User contains a special lock on change and set
     * operations to make them atomic.
     * @param userData user data string.
     * @return user instance
     * @throws ExchangeException in case invalid format of userData.
     */
    @Override
    public User create(final String userData) throws ExchangeException {
        User user = super.create(userData);
        return new User(user) {
            private Lock lock = new ReentrantLock();

            @Override
            public void changeBalance(final Currency value)
                    throws ExchangeException {
                lock.lock();
                try {
                    super.changeBalance(value);
                } catch (Exception e) {
                    lock.unlock();
                    throw e;
                }
                lock.unlock();
            }

            @Override
            public void setCurrency(final Currency currency) {
                lock.lock();
                try {
                    super.setCurrency(currency);
                } catch (Exception e) {
                    lock.unlock();
                    throw e;
                }
                lock.unlock();
            }
        };
    }
}
