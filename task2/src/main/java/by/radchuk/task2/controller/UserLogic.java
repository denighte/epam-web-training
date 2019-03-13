package by.radchuk.task2.controller;

import by.radchuk.task2.context.EnvironmentContext;
import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import by.radchuk.task2.stock.HttpsStockExchange;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

/**
 * implements user logic.
 */
@Slf4j
public final class UserLogic implements Callable<Void> {
    /**
     * currency number.
     */
    private static final int CURRENCY_NUMBER = 3;
    /**
     * random number order.
     */
    private static final int RANDOM_NUMBER_ORDER = 100;
    /**
     * await time.
     */
    private static final int AWAIT_TIME = 1000;
    /**
     * user.
     */
    private User user;
    /**
     * environment context, contains information about environment.
     */
    private final EnvironmentContext context = EnvironmentContext.getInstance();
    /**
     * Stock Exchange impl.
     */
    private final HttpsStockExchange stockExchange
            = HttpsStockExchange.getInstance();

    /**
     * default constructor.
     * @param usr user, which will be operated.
     */
    public UserLogic(final User usr) {
        user = usr;
    }
    @Override
    public Void call() throws Exception {
        log.info("Starting user logic class, with user id={}", user.getId());
        try {
            while (true) {
                int currencyToSell = (int) (Math.random() * RANDOM_NUMBER_ORDER)
                        % CURRENCY_NUMBER;
                int currencyToBuy = (int) (Math.random() * RANDOM_NUMBER_ORDER)
                        % CURRENCY_NUMBER;
                BigDecimal amountToTrande = context
                        .getMaxTransactionAmount()
                        .multiply(BigDecimal.valueOf(Math.random()));
                stockExchange.sellCurrency(
                        user,
                        new Currency(
                                getCurrency(currencyToSell),
                                amountToTrande
                        ),
                        getCurrency(currencyToBuy)
                );

                Thread.sleep(AWAIT_TIME);
            }
        } catch (Exception e) {
            log.error("Thread ended with exception:", e);
        }
        return null;
    }

    /**
     * int to currency function.
     * @param number number to convert.
     * @return CurrencyType matching number.
     */
    private CurrencyType getCurrency(final int number) {
        return CurrencyType
        .valueOf(context.getExchangeRateTable().getCurrencyOrder()[number]);
    }
}
