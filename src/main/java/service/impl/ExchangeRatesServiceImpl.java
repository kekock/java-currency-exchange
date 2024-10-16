package service.impl;

import dto.ExchangeRatesDTO;
import entity.Currencies;
import entity.ExchangeRates;
import exceptions.*;
import mapper.ExchangeRatesMapper;
import repository.impl.CurrenciesRepositoryImpl;
import repository.impl.ExchangeRatesRepositoryImpl;
import service.CurrencyExchangeService;
import utils.CurrencyIdExtractor;
import utils.ExchangeRateResponseBuilder;
import utils.Validation;

import java.math.BigDecimal;
import java.util.List;

public class ExchangeRatesServiceImpl implements CurrencyExchangeService<ExchangeRatesDTO> {

    private final ExchangeRatesRepositoryImpl exchangeRatesRepository;
    private final CurrenciesRepositoryImpl currenciesRepository;

    public ExchangeRatesServiceImpl() {
        this.currenciesRepository = new CurrenciesRepositoryImpl();
        this.exchangeRatesRepository = new ExchangeRatesRepositoryImpl();
    }

    public List<ExchangeRatesDTO> findAll() {
        List<ExchangeRates> exchangeRates = exchangeRatesRepository.findAll();
        List<Currencies> currencies = currenciesRepository.findAll();

        return ExchangeRatesMapper.toDTOList(exchangeRates, currencies);
    }

    @Override
    public ExchangeRatesDTO findByCode(String pathInfo) {
        if (!Validation.isValidPairPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        String pair = Validation.extractPairCode(pathInfo);
        ExchangeRates exchangeRate = exchangeRatesRepository.findByCode(pair);

        if (exchangeRate == null) {
            throw new NotFoundException();
        }

        return ExchangeRateResponseBuilder.assembleExchangeRateDTO(pair.substring(0, 3),
                pair.substring(3, 6), exchangeRate);
    }

    @Override
    public ExchangeRatesDTO save(String base, String target, String rate) {
        if (!Validation.areValidExchangeRateFormFields(base, target, rate)) {
            throw new MissingFormFieldsException();
        }

        if (base.equals(target)) {
            throw new SameCodeException();
        }

        if (exchangeRatesRepository.findByCode(base + target) != null) {
            throw new AlreadyExistsException();
        }

        if (currenciesRepository.findByCode(base) == null
                || currenciesRepository.findByCode(target) == null) {
            throw new NotFoundException();
        }

        BigDecimal rateValue = new BigDecimal(rate.replace(',', '.'));
        ExchangeRates newExchangeRate = new ExchangeRates(0,
                CurrencyIdExtractor.getCurrencyIdByCode(base),
                CurrencyIdExtractor.getCurrencyIdByCode(target),
                rateValue);

        ExchangeRates addedExchangeRate = exchangeRatesRepository.save(newExchangeRate);

        return ExchangeRateResponseBuilder.assembleExchangeRateDTO(base, target, addedExchangeRate);
    }

    @Override
    public void delete(String pathInfo) {
        if (!Validation.isValidPairPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        String exchangeCode = Validation.extractPairCode(pathInfo);

        if (exchangeRatesRepository.findByCode(exchangeCode) == null) {
            throw new NotFoundException();
        }

        exchangeRatesRepository.delete(exchangeCode);
    }

    @Override
    public ExchangeRatesDTO update(String base, String target, String rate) {

        if (!Validation.areValidExchangeRateFormFields(base, target, rate)) {
            throw new MissingFormFieldsException();
        }

        BigDecimal rateValue = new BigDecimal(rate.replace(',', '.'));
        ExchangeRates existingExchangeRate = exchangeRatesRepository.findByCode(base + target);

        if (existingExchangeRate == null) {
            throw new NotFoundException();
        }

        if (rateValue.equals(existingExchangeRate.getRate())) {
            throw new NotModifiedException();
        }
        existingExchangeRate.setRate(rateValue);
        ExchangeRates updatedExchangeRate = exchangeRatesRepository.update(existingExchangeRate);

        return ExchangeRateResponseBuilder.assembleExchangeRateDTO(base, target, updatedExchangeRate);
    }
}
