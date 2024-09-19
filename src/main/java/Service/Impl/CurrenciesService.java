package Service.Impl;

import Entity.Currencies;
import Repository.Impl.CurrenciesRepository;
import Service.CurrencyExchangeService;

import java.util.List;

public class CurrenciesService implements CurrencyExchangeService<Currencies> {

    private CurrenciesRepository repository;

    public CurrenciesService() {
        this.repository = new CurrenciesRepository();
    }

    @Override
    public List<Currencies> findAll() {
        return repository.findAll();
    }

    @Override
    public Currencies findByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public void save(Currencies entity) {
        repository.save(entity);
    }

    @Override
    public void delete(String code) {
        repository.delete(code);
    }

    @Override
    public void update(Currencies entity) {
        repository.update(entity);
    }
}
