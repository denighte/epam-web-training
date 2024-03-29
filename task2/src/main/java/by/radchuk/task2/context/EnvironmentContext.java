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
     * Singleton holder.
     */
    private static final class SingletonHolder {
        /**
         * Singleton.
         */
        private static final EnvironmentContext INSTANCE
                = new EnvironmentContext();
    }

    /**
     * Currencies number.
     */
    private static final int CURRENCIES_NUMBER = 3;

    /**
     * Singleton get instance.
     * @return Singleton instance.
     */
    public static EnvironmentContext getInstance() {
        return SingletonHolder.INSTANCE;
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
            log.info("Setting up environment context ...");
            try (BufferedReader reader = Files
                    .newBufferedReader(path, Charset.defaultCharset())) {

                log.info("Reading currency order ...");
                //reading currency order.
                List<String> currencyOrder = new ArrayList<>();
                for (String currency : reader.readLine().split("\\s+")) {
                    if (!currency.equals("")) {
                        currencyOrder.add(currency);
                    }
                }


                log.info("Reading currency table ...");
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
                log.info("Creating exchange rate table ...");
                exchangeRateTable = new ExchangeRateTable(
                        currencyOrder.stream().toArray(String[]::new),
                        currencyTable
                );
                log.info("Exchange rate table created");

                log.info("Reading user data ...");
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
                log.info("Finished reading property file.");
                log.info("Environment context created.");
            } catch (IOException exception) {
                log.error("Can't read data file for EnvironmentContext!");
            }
        }
    }
}
