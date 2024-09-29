package Service;

import java.util.List;

public interface CurrencyExchangeService<T> {
    List<T> findAll();
    T findByCode(String code);
    T save(String param1, String param2, String param3);
    void delete(String code);
    T update(String param1, String param2, String param3);
}