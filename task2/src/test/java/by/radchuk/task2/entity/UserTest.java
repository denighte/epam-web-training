package by.radchuk.task2.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * User test.
 */
public class UserTest {
    private User user;

    @BeforeClass
    void setUP() {
        List<Currency> currencies = new ArrayList<Currency>();
        currencies.add(new Currency(CurrencyType.BYN, BigDecimal.valueOf(100d)));
        currencies.add(new Currency(CurrencyType.USD, BigDecimal.valueOf(100d)));
        currencies.add(new Currency(CurrencyType.EUR, BigDecimal.valueOf(100d)));
        user = new User(0, currencies);
    }

    @DataProvider
    Object[][] generalChangeTestProvider() {
        return new Object[][] {
                {new Currency(CurrencyType.USD, BigDecimal.valueOf(-15d)), BigDecimal.valueOf(85d)},
                {new Currency(CurrencyType.USD, BigDecimal.valueOf(-0.5d)), BigDecimal.valueOf(84.5d)},
                {new Currency(CurrencyType.BYN, BigDecimal.valueOf(-99.9d)), BigDecimal.valueOf(0.1d)},
                {new Currency(CurrencyType.USD, BigDecimal.valueOf(81d)), BigDecimal.valueOf(165.5d)},
                {new Currency(CurrencyType.USD, BigDecimal.valueOf(0.5d)), BigDecimal.valueOf(166d)},
                {new Currency(CurrencyType.BYN, BigDecimal.valueOf(79.9d)), BigDecimal.valueOf(80d)}
        };
    }

    @Test(dataProvider = "generalChangeTestProvider")
    void generalReduceTest(Currency transaction, BigDecimal expected) throws Exception{
        user.changeBalance((transaction));
        Assert.assertEquals(user.getCurrency(transaction.getType()).getAmount(), expected);
    }
}
