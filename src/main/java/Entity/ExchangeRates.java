package Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ExchangeRates {
    private int id;
    private int baseCurrencyId;
    private int targetCurrencyId;
    private BigDecimal exchangeRate;
}
