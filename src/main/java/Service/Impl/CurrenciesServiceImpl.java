package Service.Impl;

import Exceptions.*;
import Utils.Validation;
import DTO.CurrenciesDTO;
import Entity.Currencies;
import Mapper.CurrenciesMapper;
import Repository.Impl.CurrenciesRepositoryImpl;

import Service.CurrencyExchangeService;

import java.util.List;
import java.util.NoSuchElementException;

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
            throw new InvalidCurrencyCodeException();
        }

        String currencyCode = Validation.extractCurrencyCode(pathInfo);
        Currencies currency = repository.findByCode(currencyCode);

        if (currency == null) {
            throw new CurrencyNotFoundException();
        }

        return CurrenciesMapper.toDTO(currency);
    }

    @Override
    public CurrenciesDTO save(String name, String code, String sign) {
        if(!Validation.areValidFormFields(name, code, sign)){
            throw new InvalidCurrencyCodeException();
        }

        if(repository.findByCode(code) != null){
            throw new CurrencyAlreadyExistsException();
        }

        Currencies currency = new Currencies(0, code, name, sign);
        repository.save(currency);

        Currencies addedCurrency = repository.findByCode(code);

        return CurrenciesMapper.toDTO(addedCurrency);

    }

    @Override
    public void delete(String pathInfo) {
        if (!Validation.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCurrencyCodeException();
        }

        String currencyCode = Validation.extractCurrencyCode(pathInfo);

        if (repository.findByCode(currencyCode) == null) {
            throw new CurrencyNotFoundException();
        }

        repository.delete(currencyCode);
    }

    @Override
    public CurrenciesDTO update(String pathInfo, String name, String sign) {
        if (!Validation.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCurrencyCodeException();
        }

        if (Validation.isNullOrEmpty(name) || Validation.isNullOrEmpty(sign)) {
            throw new MissingFormFieldsException();
        }

        String currencyCode = Validation.extractCurrencyCode(pathInfo);
        Currencies existingCurrency = repository.findByCode(currencyCode);

        if (existingCurrency == null) {
            throw new CurrencyNotFoundException();
        }

        if (name.equals(existingCurrency.getFullName()) && sign.equals(existingCurrency.getSign())) {
            throw new CurrencyNotModifiedException();
        }

        existingCurrency.setFullName(name);
        existingCurrency.setSign(sign);

        repository.update(existingCurrency);

        return CurrenciesMapper.toDTO(existingCurrency);
    }
}
