package Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExchangeRates {
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private BigDecimal exchangeRate;
}
