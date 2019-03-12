package by.radchuk.task2.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * contains currency type and currency amount values.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Currency {
    /**
     * currency type.
     */
    private CurrencyType type;
    /**
     * currency amount.
     */
    private BigDecimal amount;
}
