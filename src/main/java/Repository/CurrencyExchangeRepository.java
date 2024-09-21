package Repository;

import java.util.List;

public interface CurrencyExchangeRepository<T> {
    List<T> findAll();
    T findByCode(String param);
    void save(T entity);
    void delete(String param);
    void update(T entity);
}