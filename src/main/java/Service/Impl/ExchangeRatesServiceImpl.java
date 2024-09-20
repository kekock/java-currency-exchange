package Service.Impl;

import DTO.ExchangeRatesDTO;
import Entity.Currencies;
import Entity.ExchangeRates;
import Mapper.ExchangeRatesMapper;
import Repository.Impl.CurrenciesRepositoryImpl;
import Repository.Impl.ExchangeRatesRepositoryImpl;
import Service.CurrencyExchangeService;

import java.util.List;

public class ExchangeRatesServiceImpl implements CurrencyExchangeService<ExchangeRatesDTO> {

    private final ExchangeRatesRepositoryImpl exchangeRatesRepository;
    private final CurrenciesRepositoryImpl currenciesRepository;

    public ExchangeRatesServiceImpl() {
        this.currenciesRepository = new CurrenciesRepositoryImpl();
        this.exchangeRatesRepository = new ExchangeRatesRepositoryImpl();
    }

    public List<ExchangeRatesDTO> findAll() {
        List<ExchangeRates> exchangeRatesList = exchangeRatesRepository.findAll();
        List<Currencies> currenciesList = currenciesRepository.findAll();
        return null;
    }

    @Override
    public ExchangeRatesDTO findByCode(String code) {
        return null;
    }

    @Override
    public ExchangeRatesDTO save(String name, String code, String sign) {
        return null;
    }

    @Override
    public void delete(String code) {
    }

    @Override
    public ExchangeRatesDTO update(String pathInfo, String code, String sign) {
        return null;
    }
}
