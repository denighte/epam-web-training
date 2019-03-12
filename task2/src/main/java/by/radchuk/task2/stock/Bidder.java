package by.radchuk.task2.stock;

import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.concurrent.Future;

/**
 * participant of stock exchange.
 * holds by stock exchange, when selling a currencyToSell.
 */
public class Bidder {
    /**
     * user who sells currencyToSell.
     */
    @Getter
    private User user;
    /**
     * currencyToSell to sell.
     */
    @Getter
    private Currency currencyToSell;
    /**
     * currencyToSell type to buy.
     */
    @Getter
    private CurrencyType currencyTypeToBuy;
    /**
     * transaction of this user.
     */
    @Getter
    @Setter
    private Future<BigDecimal> transaction = null;

    /**
     * default constructor.
     * @param participant future participant of the stock exchange.
     * @param toSell currencyToSell to sell.
     */
    public Bidder(final User participant,
                  final Currency toSell,
                  final CurrencyType toBuy) {
        user = participant;
        currencyToSell = new Currency(toSell);
        currencyTypeToBuy = toBuy;
    }
}
