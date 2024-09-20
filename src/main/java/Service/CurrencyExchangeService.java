package Service;

import java.util.List;

public interface CurrencyExchangeService<T> {
    List<T> findAll();
    T findByCode(String pathInfo);
    T save(String name, String code, String sign);
    void delete(String code);
    T update(String pathInfo, String code, String sign);
}
