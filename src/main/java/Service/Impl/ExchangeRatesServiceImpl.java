package Service.Impl;

import DTO.ExchangeRatesDTO;
import Entity.Currencies;
import Entity.ExchangeRates;
import Exceptions.*;
import Mapper.ExchangeRatesMapper;
import Repository.Impl.CurrenciesRepositoryImpl;
import Repository.Impl.ExchangeRatesRepositoryImpl;
import Service.CurrencyExchangeService;
import Utils.CurrencyIdExtractor;
import Utils.ResponseBuildExchangeRateDTO;
import Utils.ValidationExchangeRate;

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
        if (!ValidationExchangeRate.isValidPairPath(pathInfo)){
            throw new InvalidCodeException();
        }

        String pair = ValidationExchangeRate.extractPairCode(pathInfo);
        ExchangeRates exchangeRate = exchangeRatesRepository.findByCode(pair);

        if (exchangeRate == null){
            throw new NotFoundException();
        }

        return ResponseBuildExchangeRateDTO.buildExchangeRateDTO(pair.substring(0, 3),
                pair.substring(3, 6), exchangeRate);
    }

    @Override
    public ExchangeRatesDTO save(String base, String target, String rate) {
        if (!ValidationExchangeRate.areValidFormFields(base, target, rate)){
            throw new MissingFormFieldsException();
        }

        if (exchangeRatesRepository.findByCode(base+target) != null){
            throw new AlreadyExistsException();
        }

        if (currenciesRepository.findByCode(base) == null
                || currenciesRepository.findByCode(target) == null){
            throw new NotFoundException();
        }

        BigDecimal rateValue = new BigDecimal(rate);
        ExchangeRates newExchangeRate = new ExchangeRates(0,
                CurrencyIdExtractor.getCurrencyIdByCode(base),
                CurrencyIdExtractor.getCurrencyIdByCode(target),
                rateValue);

        exchangeRatesRepository.save(newExchangeRate);
        ExchangeRates addedExchangeRate = exchangeRatesRepository.findByCode(base + target);

        return ResponseBuildExchangeRateDTO.buildExchangeRateDTO(base, target, addedExchangeRate);
    }

    @Override
    public void delete(String pathInfo) {
        if (!ValidationExchangeRate.isValidPairPath(pathInfo)){
            throw new InvalidCodeException();
        }

        String exchangeCode = ValidationExchangeRate.extractPairCode(pathInfo);

        if (exchangeRatesRepository.findByCode(exchangeCode) == null){
            throw new NotFoundException();
        }

        exchangeRatesRepository.delete(exchangeCode);
    }

    @Override
    public ExchangeRatesDTO update(String base, String target, String rate) {
        BigDecimal rateValue = new BigDecimal(rate);

        if (!ValidationExchangeRate.areValidFormFields(base, target, rate)){
            throw new MissingFormFieldsException();
        }

        ExchangeRates existingExchangeRate = exchangeRatesRepository.findByCode(base+target);

        if (existingExchangeRate == null){
            throw new NotFoundException();
        }

        if (rateValue.equals(existingExchangeRate.getRate())){
            throw new NotModifiedException();
        }
        existingExchangeRate.setRate(rateValue);
        exchangeRatesRepository.update(existingExchangeRate);

        return ResponseBuildExchangeRateDTO.buildExchangeRateDTO(base, target, existingExchangeRate);
    }
}
