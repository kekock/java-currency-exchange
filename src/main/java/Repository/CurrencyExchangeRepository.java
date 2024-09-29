package Repository;

import java.util.List;

public interface CurrencyExchangeRepository<T> {
    List<T> findAll();
    T findByCode(String param);
    T save(T entity);
    void delete(String param);
    T update(T entity);
}