package DTO;

import java.math.BigDecimal;

public class CurrencyExchangeResultDTO {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal amount;
    private BigDecimal rate;
    private BigDecimal convertedAmount;
}