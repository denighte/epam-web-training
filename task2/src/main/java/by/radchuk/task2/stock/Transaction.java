package by.radchuk.task2.stock;

import by.radchuk.task2.context.EnvironmentContext;
import by.radchuk.task2.context.ExchangeRateTable;
import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

/**
 * transaction class.
 */
@Slf4j
@AllArgsConstructor
class Transaction implements Callable<BigDecimal> {
    private User seller;
    private User buyer;
    private CurrencyType sellerCurrencyType;
    private CurrencyType buyerCurrencyType;
    private BigDecimal toBuy;

    /**
     * makes a transaction.
      * @return amount of sold currency.
     * @throws Exception in case transaction exception.
     */
    @Override
    public BigDecimal call() throws Exception {
        EnvironmentContext context = EnvironmentContext.getInstance();
        ExchangeRateTable table = context.getExchangeRateTable();
        BigDecimal toSell = toBuy.multiply(
                table.getRate(buyerCurrencyType, sellerCurrencyType)
        );
        Thread.sleep((int) (Math.random() * context.getMaxTransactionDelay()));
        try {
            buyer.changeBalance(
                    new Currency(buyerCurrencyType, toBuy.negate())
            );
            seller.changeBalance(
                    new Currency(sellerCurrencyType, toSell.negate())
            );
        } catch (Exception exception) {
            log.error("Transaction between Users, with id_1={} "
                            + "and id_2={} failed!",
                    seller.getId(), buyer.getId());
            toSell = BigDecimal.ZERO;
        }
        seller.changeBalance(new Currency(buyerCurrencyType, toBuy));
        return toSell;
    }
}
