package Service.Impl;

import Repository.CurrencyRepository;
import Service.CurrencyExchangeService;

import java.util.List;
import java.util.Optional;

public class ExchangeRatesService implements CurrencyExchangeService {
    private final CurrencyRepository repository;

    public ExchangeRatesService(CurrencyRepository repository) {
        this.repository = repository;
    }

    @Override
    public List findAll() {
        return List.of();
    }

    @Override
    public Optional findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public void save(Object entity) {

    }

    @Override
    public void update(Object entity) {

    }

    //todo exchange
}
