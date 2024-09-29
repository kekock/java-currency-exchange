package Utils;

import Entity.Currencies;
import Exceptions.NotFoundException;
import Repository.Impl.CurrenciesRepositoryImpl;

public class CurrencyIdExtractor {

    private static final String ERROR_CURRENCY_NOT_FOUND = "Currency with code '%s' not found.";

    private static final CurrenciesRepositoryImpl repository = new CurrenciesRepositoryImpl();

    public static int getCurrencyIdByCode(String code) {
        Currencies currency = repository.findByCode(code);

        if (currency == null) {
            throw new NotFoundException(String.format(ERROR_CURRENCY_NOT_FOUND, code));
        }

        return currency.getId();
    }
}