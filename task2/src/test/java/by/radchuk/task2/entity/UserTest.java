package by.radchuk.task2.entity;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User test.
 */
public class UserTest {
    private User user;

    @BeforeClass
    void setUP() {
        List<Pair<Currency, BigDecimal>> currencies = new ArrayList<Pair<Currency, BigDecimal>>();
        currencies.add(new Pair<Currency, BigDecimal>(Currency.BYN, BigDecimal.valueOf(100d)));
        currencies.add(new Pair<Currency, BigDecimal>(Currency.USD, BigDecimal.valueOf(100d)));
        currencies.add(new Pair<Currency, BigDecimal>(Currency.EUR, BigDecimal.valueOf(100d)));
        user = new User(0, currencies);
    }

    @DataProvider
    Object[][] generalReduceTestProvider() {
        return new Object[][] {
                {new Pair<Currency, BigDecimal>(Currency.USD, BigDecimal.valueOf(15d)), BigDecimal.valueOf(85d)},
                {new Pair<Currency, BigDecimal>(Currency.USD, BigDecimal.valueOf(0.5d)), BigDecimal.valueOf(84.5d)},
                {new Pair<Currency, BigDecimal>(Currency.BYN, BigDecimal.valueOf(99.9d)), BigDecimal.valueOf(0.1d)}
        };
    }

    @DataProvider
    Object[][] generalRechargeTestProvider() {
        return new Object[][] {
                {new Pair<Currency, BigDecimal>(Currency.USD, BigDecimal.valueOf(81d)), BigDecimal.valueOf(165.5d)},
                {new Pair<Currency, BigDecimal>(Currency.USD, BigDecimal.valueOf(0.5d)), BigDecimal.valueOf(166d)},
                {new Pair<Currency, BigDecimal>(Currency.BYN, BigDecimal.valueOf(79.9d)), BigDecimal.valueOf(80d)}
        };
    }

    @Test(dataProvider = "generalReduceTestProvider")
    void generalReduceTest(Pair<Currency, BigDecimal> transaction, BigDecimal expected) throws Exception{
        user.reduceBalance(transaction);
        Map<Currency, BigDecimal> map = user.getCurrencyMap();
        Assert.assertEquals(map.get(transaction.getKey()), expected);
    }

    @Test(dataProvider = "generalRechargeTestProvider", dependsOnMethods = {"generalReduceTest"})
    void generalRechargeTest(Pair<Currency, BigDecimal> transaction, BigDecimal expected) throws Exception{
        user.rechargeBalance(transaction);
        Map<Currency, BigDecimal> map = user.getCurrencyMap();
        Assert.assertEquals(map.get(transaction.getKey()), expected);
    }
}
