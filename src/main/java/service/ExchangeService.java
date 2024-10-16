package service;

import dto.ExchangeResultDTO;
import entity.ExchangeRates;
import exceptions.InvalidCodeException;
import exceptions.MissingFormFieldsException;
import exceptions.NotFoundException;
import exceptions.SameCodeException;
import mapper.CurrenciesMapper;
import repository.impl.CurrenciesRepositoryImpl;
import repository.impl.ExchangeRatesRepositoryImpl;
import utils.Validation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {

    private final ExchangeRatesRepositoryImpl exchangeRatesRepository;
    private final CurrenciesRepositoryImpl currenciesRepository;

    public ExchangeService() {
        this.currenciesRepository = new CurrenciesRepositoryImpl();
        this.exchangeRatesRepository = new ExchangeRatesRepositoryImpl();
    }

    public ExchangeResultDTO convertCurrency(String from, String to, String amount) {

        validateRequest(from, to, amount);

        BigDecimal amountValue = new BigDecimal(amount.replace(',', '.'));
        BigDecimal rate = getRate(from, to);

        System.out.println(rate);

        if (rate == null) {
            throw new NotFoundException();
        }

        BigDecimal convertedAmount = amountValue.multiply(rate).setScale(2, RoundingMode.HALF_UP);

        return new ExchangeResultDTO(
                CurrenciesMapper.toDTO(currenciesRepository.findByCode(from)),
                CurrenciesMapper.toDTO(currenciesRepository.findByCode(to)),
                rate,
                amountValue,
                convertedAmount
        );
    }

    private void validateRequest(String from, String to, String amount) {

        if (!Validation.areValidExchangeRateFormFields(from, to, amount)) {
            throw new MissingFormFieldsException();
        }

        if (!Validation.isValidPairCode(from + to)) {
            throw new InvalidCodeException();
        }

        if (from.equals(to)) {
            throw new SameCodeException();
        }
    }

    private BigDecimal getRate(String from, String to) {
        BigDecimal rate = getDirectRate(from, to);

        if (rate == null) {
            rate = getReverseRate(from, to);
        }

        if (rate == null) {
            rate = getRateThroughUsd(from, to);
        }

        return rate;
    }

    private BigDecimal getDirectRate(String from, String to) {
        ExchangeRates exchangeRate = exchangeRatesRepository.findByCode(from + to);
        if (exchangeRate != null) {
            return exchangeRate.getRate();
        } else {
            return null;
        }
    }

    private BigDecimal getReverseRate(String from, String to) {
        ExchangeRates exchangeRate = exchangeRatesRepository.findByCode(to + from);
        if (exchangeRate != null) {
            return BigDecimal.ONE.divide(exchangeRate.getRate(), 4, RoundingMode.HALF_UP);
        } else {
            return null;
        }
    }

    private BigDecimal getRateThroughUsd(String from, String to) {
        String USD_CODE = "USD";
        ExchangeRates fromUsdExchangeRate = exchangeRatesRepository.findByCode(USD_CODE + from);
        ExchangeRates toUsdExchangeRate = exchangeRatesRepository.findByCode(USD_CODE + to);

        if (fromUsdExchangeRate != null && toUsdExchangeRate != null) {
            return toUsdExchangeRate.getRate().divide(fromUsdExchangeRate.getRate(), 4, RoundingMode.HALF_UP);
        } else {
            return null;
        }
    }
}