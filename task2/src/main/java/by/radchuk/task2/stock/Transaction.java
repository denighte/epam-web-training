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
    /**
     * currency seller.
     */
    private User seller;
    /**
     * currency buyer.
     */
    private User buyer;
    /**
     * seller currency type.
     */
    private CurrencyType sellerCurrencyType;
    /**
     * buyer currency type.
     */
    private CurrencyType buyerCurrencyType;
    /**
     * how much to buy.
     */
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

        Currency oldBuyerBalance
                = new Currency(buyer.getCurrency(buyerCurrencyType));
        Currency oldSellerBalance
                = new Currency(seller.getCurrency(sellerCurrencyType));

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
            buyer.setCurrency(oldBuyerBalance);
            seller.setCurrency(oldSellerBalance);
            return BigDecimal.ZERO;
        }
        seller.changeBalance(new Currency(buyerCurrencyType, toBuy));
        log.info("Transaction ended with, and user with id={} sold: {}",
                seller.getId(), toSell);
        return toSell;
    }
}
