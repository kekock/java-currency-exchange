package Service.Impl;

import Exceptions.*;
import Utils.ValidationCurrency;
import DTO.CurrenciesDTO;
import Entity.Currencies;
import Mapper.CurrenciesMapper;
import Repository.Impl.CurrenciesRepositoryImpl;

import Service.CurrencyExchangeService;

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
        if (!ValidationCurrency.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        String currencyCode = ValidationCurrency.extractCurrencyCode(pathInfo);
        Currencies currency = repository.findByCode(currencyCode);

        if (currency == null) {
            throw new NotFoundException();
        }

        return CurrenciesMapper.toDTO(currency);
    }

    @Override
    public CurrenciesDTO save(String name, String code, String sign) {
        if(!ValidationCurrency.areValidFormFields(name, code, sign)){
            throw new MissingFormFieldsException();
        }

        if(repository.findByCode(code) != null){
            throw new AlreadyExistsException();
        }

        Currencies currency = new Currencies(0, code, name, sign);
        repository.save(currency);

        Currencies addedCurrency = repository.findByCode(code);

        return CurrenciesMapper.toDTO(addedCurrency);

    }

    @Override
    public void delete(String pathInfo) {
        if (!ValidationCurrency.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        String currencyCode = ValidationCurrency.extractCurrencyCode(pathInfo);

        if (repository.findByCode(currencyCode) == null) {
            throw new NotFoundException();
        }

        repository.delete(currencyCode);
    }

    @Override
    public CurrenciesDTO update(String pathInfo, String name, String sign) {
        if (!ValidationCurrency.isValidCurrencyPath(pathInfo)) {
            throw new InvalidCodeException();
        }

        if (ValidationCurrency.isNullOrEmpty(name) || ValidationCurrency.isNullOrEmpty(sign)) {
            throw new MissingFormFieldsException();
        }

        String currencyCode = ValidationCurrency.extractCurrencyCode(pathInfo);
        Currencies existingCurrency = repository.findByCode(currencyCode);

        if (existingCurrency == null) {
            throw new NotFoundException();
        }

        if (name.equals(existingCurrency.getFullName()) && sign.equals(existingCurrency.getSign())) {
            throw new NotModifiedException();
        }

        existingCurrency.setFullName(name);
        existingCurrency.setSign(sign);

        repository.update(existingCurrency);

        return CurrenciesMapper.toDTO(existingCurrency);
    }
}
