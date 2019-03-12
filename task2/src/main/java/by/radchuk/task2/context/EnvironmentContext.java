package by.radchuk.task2.context;

import by.radchuk.task2.entity.CurrencyType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Context of the environment.
 */
@Data
public final class EnvironmentContext {
    /**
     * Singleton.
     */
    public static final EnvironmentContext INSTANCE = new EnvironmentContext();

    /**
     * private constructor to prevent direct class creation.
     */
    private EnvironmentContext() { }

    /**
     * Number of the users in the application.
     */
    private int userNumber;

}
