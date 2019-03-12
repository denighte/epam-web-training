package by.radchuk.task2.context;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class EnvironmentContextTest {
    EnvironmentContext context;
    BigDecimal[][] expectedTable = {
        {BigDecimal.valueOf(1), BigDecimal.valueOf(0.885), BigDecimal.valueOf(2.127)},
        {BigDecimal.valueOf(1.13), BigDecimal.valueOf(1), BigDecimal.valueOf(2.41)},
        {BigDecimal.valueOf(0.47), BigDecimal.valueOf(0.42), BigDecimal.valueOf(1)}
    };

    String[] expectedUserData = {
        "{id:1, US:150, EUR:200, BYN:300}",
        "{id:1, US:100, EUR:200, BYN:300}",
        "{id:1, US:100, EUR:200, BYN:400}",
        "{id:1, US:200, EUR:100, BYN:300}",
        "{id:1, US:500, EUR:200, BYN:300}"
    };

    @Test
    @Parameters({"readerTestFile"})
    void readTest() {
        context = EnvironmentContext.getInstance();
    }

    @Test(dependsOnMethods = {"readTest"})
    void exchangeRateTableTest() {
        BigDecimal[][] rateTable = getRawTableView(context.getExchangeRateTable());
        for(int i = 0; i < rateTable.length; ++i) {
            Assert.assertEquals(rateTable[i], expectedTable[i]);
        }
    }

    @Test
    void userNumberTest() {
        Assert.assertEquals(context.getUserNumber(), 5);
    }

    @Test(dependsOnMethods = "userNumberTest")
    void userDataTest() {
        Assert.assertEquals(context.getUsersData(), expectedUserData);
    }

    private BigDecimal[][] getRawTableView(ExchangeRateTable rateTable) {
        try {
            Field table = rateTable.getClass().getDeclaredField("table");
            table.setAccessible(true);
            return (BigDecimal[][])table.get(rateTable);
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
            //ignore
        }
        return null;
    }
}
