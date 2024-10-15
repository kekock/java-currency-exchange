package utils;

import dto.ExchangeRatesDTO;
import entity.Currencies;
import entity.ExchangeRates;
import mapper.ExchangeRatesMapper;
import repository.impl.CurrenciesRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExchangeRateResponseBuilder {

    private static final CurrenciesRepositoryImpl repository = new CurrenciesRepositoryImpl();

    public static ExchangeRatesDTO assembleExchangeRateDTO(String base, String target, ExchangeRates exchangeRate) {
        List<Currencies> currencies = Stream.of(
                repository.findByCode(base),
                repository.findByCode(target)
        ).collect(Collectors.toList());

        return ExchangeRatesMapper.toDTO(exchangeRate, currencies);
    }
}
