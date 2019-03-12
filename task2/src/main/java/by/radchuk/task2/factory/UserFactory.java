package by.radchuk.task2.factory;

import by.radchuk.task2.entity.Currency;
import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import by.radchuk.task2.exception.ExchangeException;
import by.radchuk.task2.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User factory.
 * Creates user instance from string.
 * User data format matches regex:
 * \\{id:(\\d+),USD:(\\d+(?:\\.\\d+)?),
 * EUR:(\\d+(?:\\.\\d+)?),BYN:(\\d+(?:\\.\\d+)?)\\}
 */
@Slf4j
public class UserFactory {
    /**
     * validator.
     * used to check the correctness of the user.
     */
    private UserValidator validator = new UserValidator();
    /**
     * id regex group.
     */
    private static final int ID_GROUP = 1;
    /**
     * usd group.
     */
    private static final int USD_GROUP = 2;
    /**
     * eur group.
     */
    private static final int EUR_GROUP = 3;
    /**
     * byn group.
     */
    private static final int BYN_GROUP = 4;
    /**
     * User data format.
     */
    private static final Pattern USER_DATA_PATTERN
            = Pattern.compile("\\{id:(\\d+),"
            + "USD:(\\d+(?:\\.\\d+)?),"
            + "EUR:(\\d+(?:\\.\\d+)?),"
            + "BYN:(\\d+(?:\\.\\d+)?)\\}");
    /**
     * creates user instance.
     * @param userData user data string.
     * @return user instace.
     * @throws ExchangeException in case invalid user data.
     */
    public User create(final String userData) throws ExchangeException {
        String formatData = userData.replaceAll("\\s+", "");
        Matcher matcher = USER_DATA_PATTERN.matcher(formatData);
        if (!matcher.matches()) {
            log.error(
                    "the string didn't matched regex with userData = {}",
                    userData
            );
            throw new ExchangeException("invalid format of userData!");
        }
        List<Currency> userCurrencyList = new ArrayList<>();
        //Adding currencies to currency list.
        userCurrencyList.add(new Currency(
                CurrencyType.USD,
                new BigDecimal(matcher.group(USD_GROUP))
                ));
        userCurrencyList.add(new Currency(
                CurrencyType.EUR,
                new BigDecimal(matcher.group(EUR_GROUP))
        ));
        userCurrencyList.add(new Currency(
                CurrencyType.BYN,
                new BigDecimal(matcher.group(BYN_GROUP))
        ));
        int id = Integer.parseInt(matcher.group(ID_GROUP));

        User user = new User(id, userCurrencyList);
        if (!validator.validate(user)) {
            throw new ExchangeException(validator.getStatusMessage());
        }
        return user;
    }



}
