package by.radchuk.task2.entity;

import by.radchuk.task2.exception.ExchangeException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * exchange member.
 */
@Slf4j
@AllArgsConstructor
@EqualsAndHashCode
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
    private final List<Currency> currencies;

    /**
     * Copy constructor.
     * @param other other user.
     */
    public User(final User other) {
        id = other.id;
        currencies = new ArrayList<>(other.currencies);
    }
    /**
     * reduces balance of the user.
     * @param value currency pair.
     * @throws ExchangeException throws in case negative balance / wrong op.
     */
    public void changeBalance(final Currency value)
            throws ExchangeException {
        Currency currency = getCurrency(value.getType());
        if (currency == null) {
            log.debug("There is no such currency in User with id={}", id);
            throw new ExchangeException("There is no such currency!");
        }
        BigDecimal newBalance = currency.getAmount()
                                        .add(value.getAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            log.debug("User with id={} don't have enough money,"
                    + "transaction error", id);
            throw new ExchangeException("Not enough money!");
        }
        currency.setAmount(newBalance);
    }

    /**
     * Get currency by specified type.
     * @param type type of the currency.
     * @return currency with specified type.
     */
    public Currency getCurrency(final CurrencyType type) {
        return currencies
                .stream()
                .filter(currency -> currency.getType().equals(type))
                .findAny().orElse(null);
    }

    /**
     * sets currency with new type.
     * or updates the existing.
     * @param currency currency to set
     */
    public void setCurrency(final Currency currency) {
        Currency old = getCurrency(currency.getType());
        if (old != null) {
            old.setAmount(currency.getAmount());
            return;
        }
        currencies.add(currency);
    }
}
