package Service.Impl;

import Entity.ExchangeRates;
import Repository.Impl.ExchangeRatesRepository;
import Service.CurrencyExchangeService;

import java.util.List;

public class ExchangeRatesService implements CurrencyExchangeService<ExchangeRates> {

    private final ExchangeRatesRepository repository;

    public ExchangeRatesService() {
        this.repository = new ExchangeRatesRepository();
    }

    public List<ExchangeRates> findAll() {
        return repository.findAll();
    }

    @Override
    public ExchangeRates findByCode(String code) {
        return null;
    }

    @Override
    public void save(ExchangeRates entity) {

    }

    @Override
    public void update(ExchangeRates entity) {

    }

    @Override
    public void delete(String code) {
    }

    //todo exchange
}
