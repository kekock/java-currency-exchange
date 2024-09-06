package Service.Impl;

import Repository.Impl.CurrenciesRepository;
import Service.CurrencyExchangeService;

import java.util.List;
import java.util.Optional;

public class CurrenciesService implements CurrencyExchangeService {

    private CurrenciesRepository repository;

    public CurrenciesService() {
        this.repository = new CurrenciesRepository();
    }

    @Override
    public List findAll() {
        return repository.findAll();
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
}
