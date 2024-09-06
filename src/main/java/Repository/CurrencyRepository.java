package Repository;

import java.util.List;
import java.util.Optional;

public interface CurrencyRepository<T> {
    List<T> findAll();
    Optional<T> findByCode(String code);
    void save(T entity);
    void update(T entity);
}