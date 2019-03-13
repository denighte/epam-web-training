package by.radchuk.task2.util;

import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * Prints user with a specified await time.
 */
@AllArgsConstructor
@Slf4j
public final class UserPrinter implements Runnable {
    /**
     * thread await time.
     */
    private static final int AWAIT_TIME = 4000;
    /**
     * user list.
     */
    private List<User> users;


    @Override
    public void run() {
        while (true) {
            for (User user : users) {
                log.info(
                        "\nUser with id={} wallet status:\n"
                        + "USD={}\n"
                        + "EUR={}\n"
                        + "BYN={}\n",
                        user.getId(),
                        user.getCurrency(CurrencyType.USD),
                        user.getCurrency(CurrencyType.EUR),
                        user.getCurrency(CurrencyType.BYN)
                );
            }
            try {
                Thread.sleep(AWAIT_TIME);
            } catch (InterruptedException e) {
                return;
            }

        }
    }
}
