package Mapper;

import DTO.ExchangeRatesDTO;
import Entity.ExchangeRates;
import Entity.Currencies;
import java.util.List;
import java.util.stream.Collectors;

public class ExchangeRatesMapper {

    public static ExchangeRatesDTO toDTO(ExchangeRates exchangeRate, List<Currencies> currencies) {
        ExchangeRatesDTO dto = new ExchangeRatesDTO();
        dto.setId(exchangeRate.getId());
        dto.setRate(exchangeRate.getRate());

        Currencies baseCurrency = currencies.stream()
                .filter(c -> c.getId() == exchangeRate.getBaseCurrencyId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Base currency not found"));

        Currencies targetCurrency = currencies.stream()
                .filter(c -> c.getId() == exchangeRate.getTargetCurrencyId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Target currency not found"));

        dto.setBaseCurrency(CurrenciesMapper.toDTO(baseCurrency));
        dto.setTargetCurrency(CurrenciesMapper.toDTO(targetCurrency));

        return dto;
    }

    public static List<ExchangeRatesDTO> toDTOList(List<ExchangeRates> exchangeRates, List<Currencies> currencies) {
        return exchangeRates.stream()
                .map(exchangeRate -> toDTO(exchangeRate, currencies))
                .collect(Collectors.toList());
    }
}