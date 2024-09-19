package DTO;

import java.math.BigDecimal;

public class CurrencyExchangeRequestDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;
}
