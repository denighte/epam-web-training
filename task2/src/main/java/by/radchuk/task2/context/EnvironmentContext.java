package by.radchuk.task2.context;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Context of the environment.
 */
@Slf4j
public final class EnvironmentContext {
    /**
     * Singleton.
     * volatile because of JCM memory model feature.
     */
    private static volatile EnvironmentContext instance;

    /**
     * Currencies number.
     */
    private static final int CURRENCIES_NUMBER = 3;

    /**
     * Double Checked Locking & volatile getInstance.
     * @return Singleton instance.
     */
    public static EnvironmentContext getInstance() {
        EnvironmentContext localInstance = instance;
        if (localInstance == null) {
            synchronized (EnvironmentContext.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance;
                    localInstance = new EnvironmentContext();
                }
            }
        }
        return localInstance;
    }

    /**
     * private constructor to prevent direct class creation.
     */
    private EnvironmentContext() {
        ContextFileReader reader = new ContextFileReader();
        reader.read();
    }

    /**
     * exchange rate table.
     */
    @Getter
    private ExchangeRateTable exchangeRateTable;
    /**
     * Number of the users in the application.
     */
    @Getter
    private int userNumber;
    /**
     * users string representation of data.
     */
    @Getter
    private String[] usersData;
    /**
     * maximum amount per transaction.
     */
    @Getter
    private BigDecimal maxTransactionAmount;
    /**
     * maximum delay in transactions.
     */
    @Getter
    private int maxTransactionDelay;

    /**
     * Reads EnvironmentContext data from file.
     */
    private class ContextFileReader {
        /**
         * Path to data file.
         */
        private Path path = Paths.get("src/test/resources/data", "data.txt");

        /**
         * read data from file.
         */
        private void read() {
            try (BufferedReader reader = Files
                    .newBufferedReader(path, Charset.defaultCharset())) {

                //reading currency order.
                List<String> currencyOrder = new ArrayList<>();
                for (String currency : reader.readLine().split("\\s+")) {
                    if (!currency.equals("")) {
                        currencyOrder.add(currency);
                    }
                }


                //reading currency table.
                BigDecimal[][] currencyTable
                        = new BigDecimal[CURRENCIES_NUMBER][CURRENCIES_NUMBER];

                for (int i = 0; i < CURRENCIES_NUMBER; ++i) {
                    String[] currencyLine = reader.readLine().split("\\s+");
                    for (int j = 1; j < CURRENCIES_NUMBER + 1; ++j) {
                        currencyTable[i][j - 1]
                                = new BigDecimal(currencyLine[j]);
                    }
                }
                exchangeRateTable = new ExchangeRateTable(
                        currencyOrder.stream().toArray(String[]::new),
                        currencyTable
                );

                //reading users number.
                userNumber = Integer.parseInt(
                        reader.readLine().split("=")[1]
                                .replaceAll("\\s+", "")
                );
                usersData = new String[userNumber];
                for (int i = 0; i < userNumber; ++i) {
                    usersData[i] = reader.readLine();
                }

                maxTransactionAmount = new BigDecimal(reader.readLine()
                        .split("=")[1].replaceAll("\\s+", "")
                );
                maxTransactionDelay = Integer.parseInt(
                        reader.readLine().split("=")[1]
                                .replaceAll("\\s+", "")
                );

            } catch (IOException exception) {
                log.error("Can't read data file for EnvironmentContext!");
            }
        }
    }
}
