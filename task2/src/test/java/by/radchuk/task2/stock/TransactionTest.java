package by.radchuk.task2.stock;

import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import by.radchuk.task2.exception.ExchangeException;
import by.radchuk.task2.factory.ConcurrentUserFactory;
import by.radchuk.task2.factory.UserFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.concurrent.FutureTask;

public class TransactionTest {
    private User seller;
    private User buyer;
    private UserFactory factory;

    @BeforeClass
    void setUp() throws ExchangeException {
        factory = new ConcurrentUserFactory();
        seller = factory.create("{id: 1, USD: 10.499, EUR: 5, BYN: 0}");
        buyer = factory.create("{id: 2, USD: 5.511, EUR: 10, BYN: 10}");
    }

    @Test
    void sameCurrencyTest() throws Exception {
        FutureTask<BigDecimal> task = new FutureTask<>(
          new Transaction(
            seller,
            buyer,
            CurrencyType.USD,
            CurrencyType.USD,
            new BigDecimal("0.499")
          )
        );
        new Thread(task).start();
        task.get();
        Assert.assertEquals(seller.getCurrency(CurrencyType.USD).getAmount(), new BigDecimal("10.499"));
    }

    @Test
    void differentCurrencyTest() throws Exception {
        FutureTask<BigDecimal> task = new FutureTask<>(
                new Transaction(
                        seller,
                        buyer,
                        CurrencyType.USD,
                        CurrencyType.EUR,
                        new BigDecimal("1")
                )
        );
        new Thread(task).start();
        task.get();
        Assert.assertEquals(seller.getCurrency(CurrencyType.USD).getAmount(), new BigDecimal("9.369"));
    }
}
