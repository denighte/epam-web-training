package by.radchuk.task2.entity;

import by.radchuk.task2.exception.ExchangeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * exchange member.
 */
@Slf4j
public class User {
    /**
     * unique id of the user.
     */
    @Getter
    private final long id;
    /**
     * member's currency wallet.
     * key - type of the currency.
     * value - amount of the currency.
     */
    @Getter
    private final Map<Currency, Double> currencyMap;
    /**
     * locker for atomic operations.
     */
    private final Lock lock = new ReentrantLock();

    /**
     * Constructs user from the array of currency pairs.
     * key - type of the currency.
     * value - amount of the currency.
     * @param values currency pairs array.
     * @param userId user unique id.
     */
    public User(final long userId, final Pair<Currency, Double>[] values) {
        id = userId;
        currencyMap = new ConcurrentHashMap<>();
        for (Pair<Currency, Double> pair : values) {
            currencyMap.put(pair.getKey(), pair.getValue());
        }
    }

    /**
     * reduces balance of the user.
     * @param value currency pair.
     * @throws ExchangeException throws in case negative balance / wrong op.
     */
    public void reduceBalance(final Pair<Currency, Double> value)
            throws ExchangeException {
        if (value.getValue() < 0) {
            throw new ExchangeException("Trying to reduce negative value!");
        }
        lock.lock();
        double newBalance = currencyMap.get(value.getKey()) - value.getValue();
        if (newBalance < 0) {
            lock.unlock();
            log.debug("User with id={} don't have enough money, tr error", id);
            throw new ExchangeException("Not enough money!");
        }
        currencyMap.put(value.getKey(), newBalance);

        lock.unlock();
    }

    /**
     * recharges balance of the user.
     * @param value currency pair.
     * @throws ExchangeException throws in case negative balance / wrong op.
     */
    public void rechargeBalance(final Pair<Currency, Double> value)
            throws ExchangeException {
        if (value.getValue() < 0) {
            throw new ExchangeException("Trying to reduce negative value!");
        }
        lock.lock();
        double newBalance = currencyMap.get(value.getKey()) + value.getValue();
        currencyMap.put(value.getKey(), newBalance);
        lock.unlock();
    }
}
