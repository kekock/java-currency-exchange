package DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ExchangeRatesDTO {

    private int id;
    private CurrenciesDTO baseCurrency;
    private CurrenciesDTO targetCurrency;
    private BigDecimal rate;
}