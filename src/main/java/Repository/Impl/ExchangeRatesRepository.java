package Repository.Impl;

import Entity.ExchangeRates;
import Repository.CurrencyRepository;

import java.util.List;
import java.util.Optional;

public class ExchangeRatesRepository implements CurrencyRepository {

    @Override
    public List<ExchangeRates> findAll() {
        return List.of();
    }

    @Override
    public Optional<ExchangeRates> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public void save(Object entity) {

    }

    @Override
    public void update(Object entity) {

    }
}
