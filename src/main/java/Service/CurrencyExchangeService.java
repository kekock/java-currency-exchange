package Service;

import Entity.Currencies;

import java.util.List;

public interface CurrencyExchangeService<T> {
    List<T> findAll();
    T findByCode(String code);
    void save(T entity);
    void delete(String code);
    void update(T entity);
}
