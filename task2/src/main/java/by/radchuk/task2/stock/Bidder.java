package by.radchuk.task2.stock;

import by.radchuk.task2.entity.CurrencyType;
import by.radchuk.task2.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Bidder {
    private User user;
    private CurrencyType sellCurrencyType;
    private BigDecimal amount;
}
