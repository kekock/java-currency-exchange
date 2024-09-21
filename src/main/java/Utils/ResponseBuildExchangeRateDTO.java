package Utils;

import DTO.ExchangeRatesDTO;
import Entity.Currencies;
import Entity.ExchangeRates;
import Mapper.ExchangeRatesMapper;
import Repository.Impl.CurrenciesRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ResponseBuildExchangeRateDTO {

    private static final CurrenciesRepositoryImpl repository = new CurrenciesRepositoryImpl();

    public static ExchangeRatesDTO buildExchangeRateDTO(String base, String target, ExchangeRates exchangeRate) {
        List<Currencies> currencies = Stream.of(
                repository.findByCode(base),
                repository.findByCode(target)
        ).collect(Collectors.toList());

        return ExchangeRatesMapper.toDTO(exchangeRate, currencies);
    }
}
