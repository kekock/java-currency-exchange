package service.impl;

import exceptions.*;
import utils.Validation;
import dto.CurrenciesDTO;
import entity.Currencies;
import mapper.CurrenciesMapper;
import repository.impl.CurrenciesRepositoryImpl;

import service.CurrencyExchangeService;

import java.util.List;

public class CurrenciesServiceImpl implements CurrencyExchangeService<CurrenciesDTO> {

    private final CurrenciesRepositoryImpl repository;

    public CurrenciesServiceImpl() {
        this.repository = new CurrenciesRepositoryImpl();
    }

    @Override
    public List<CurrenciesDTO> findAll() {
        List<Currencies> currencies = repository.findAll();
        return CurrenciesMapper.toDTOList(currencies);
    }

    @Override
    public CurrenciesDTO findByCode(String pathInfo) {
        if (!Validation.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        String currencyCode = Validation.extractCurrencyCode(pathInfo);
        Currencies currency = repository.findByCode(currencyCode);

        if (currency == null) {
            throw new NotFoundException();
        }

        return CurrenciesMapper.toDTO(currency);
    }

    @Override
    public CurrenciesDTO save(String name, String code, String sign) {
        if(!Validation.areValidCurrencyFormFields(name, code, sign)){
            throw new MissingFormFieldsException();
        }

        if(repository.findByCode(code) != null){
            throw new AlreadyExistsException();
        }

        Currencies currency = new Currencies(0, code, name, sign);
        Currencies addedCurrency = repository.save(currency);

        return CurrenciesMapper.toDTO(addedCurrency);

    }

    @Override
    public void delete(String pathInfo) {
        if (!Validation.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        String currencyCode = Validation.extractCurrencyCode(pathInfo);

        if (repository.findByCode(currencyCode) == null) {
            throw new NotFoundException();
        }

        repository.delete(currencyCode);
    }

    @Override
    public CurrenciesDTO update(String pathInfo, String name, String sign) {
        if (!Validation.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        if (Validation.isNullOrEmpty(name) || Validation.isNullOrEmpty(sign)) {
            throw new MissingFormFieldsException();
        }

        String currencyCode = Validation.extractCurrencyCode(pathInfo);
        Currencies existingCurrency = repository.findByCode(currencyCode);

        if (existingCurrency == null) {
            throw new NotFoundException();
        }

        if (name.equals(existingCurrency.getFullName()) && sign.equals(existingCurrency.getSign())) {
            throw new NotModifiedException();
        }

        existingCurrency.setFullName(name);
        existingCurrency.setSign(sign);

        Currencies updatedCurrency = repository.update(existingCurrency);

        return CurrenciesMapper.toDTO(updatedCurrency);
    }
}
