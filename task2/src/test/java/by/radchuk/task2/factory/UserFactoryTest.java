package by.radchuk.task2.factory;

import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import by.radchuk.task2.exception.ExchangeException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserFactoryTest {
    private UserFactory factory;
    @BeforeClass
    void setUp() {
        factory = new UserFactory();
    }

    @DataProvider
    Object[][] generalTestProvider() {
        Currency[] userList1 = new Currency[] {
                new Currency(CurrencyType.USD, BigDecimal.valueOf(100)),
                new Currency(CurrencyType.EUR, BigDecimal.valueOf(50)),
                new Currency(CurrencyType.BYN, BigDecimal.valueOf(40.5))
        };
        Currency[] userList2 = new Currency[] {
                new Currency(CurrencyType.USD, BigDecimal.valueOf(140)),
                new Currency(CurrencyType.EUR, BigDecimal.valueOf(0.85)),
                new Currency(CurrencyType.BYN, BigDecimal.valueOf(10.74))
        };
        Currency[] userList3 = new Currency[] {
                new Currency(CurrencyType.USD, BigDecimal.valueOf(10)),
                new Currency(CurrencyType.EUR, BigDecimal.valueOf(0.76)),
                new Currency(CurrencyType.BYN, BigDecimal.valueOf(9.31))
        };
        User user1 = new User(1, Arrays.asList(userList1));
        User user2 = new User(2, Arrays.asList(userList2));
        User user3 = new User(3, Arrays.asList(userList3));

        return new Object[][] {
                {"{id:1, USD:100, EUR:50, BYN:40.5}", user1},
                {"{id:2,USD:140,EUR:0.85,BYN:10.74}", user2},
                {"{id:3,USD:10, EUR:0.76, BYN:9.31}", user3}
        };
    }

    @DataProvider
    Object[][] exceptionTestProvider() {
        return new Object[][] {
                {"{id:1.1, USD:100, EUR:50, BYN:40.5}"},
                {"{id:2, USD:0, EUR:0, BYN:0}"},
                {"{id:3, EUR:100, EUR:50, BYN:40.5}"},
                {"{id:4, USD:0123, EUR:0000, BYN:0045}"}

        };
    }

    @Test(dataProvider = "generalTestProvider")
    void generalCreateTest(String data, User user) throws ExchangeException {
        User usr = factory.create(data);
        usr.equals(user);
        System.out.println(usr.equals(user));
        Assert.assertEquals(factory.create(data), user);
    }

    @Test(dataProvider = "exceptionTestProvider", expectedExceptions = {ExchangeException.class})
    void exceptionCreateTest(String data) throws ExchangeException{
        factory.create(data);
        Assert.fail();
    }
}
