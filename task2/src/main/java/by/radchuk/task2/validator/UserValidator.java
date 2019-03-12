package by.radchuk.task2.validator;

import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * validates user data correctness.
 */
@Slf4j
public class UserValidator {
    /**
     * status message.
     */
    @Getter
    private String statusMessage = "OK";
    /**
     * validates user data correctness.
     * @param user user to validate.
     * @return true if OK, otherwise false.
     */
    public boolean validate(final User user) {
        for (CurrencyType type : CurrencyType.values()) {
            if (user.getCurrency(type)
                    .getAmount()
                    .compareTo(BigDecimal.ZERO) < 0) {
                statusMessage = "Currency amount can't be less than zero!";
                log.debug("User validation failed with status message='{}'",
                        statusMessage);
                return false;
            }
        }
        return true;
    }
}
