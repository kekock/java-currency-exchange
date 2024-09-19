package Repository.Impl;

import Entity.ExchangeRates;
import Repository.CurrencyExchangeRepository;
import Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesRepository implements CurrencyExchangeRepository<ExchangeRates> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM currencyExchange.ExchangeRates";

    private static ExchangeRates getExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRates(
                resultSet.getInt("id"),
                resultSet.getInt("base_currency_id"),
                resultSet.getInt("target_currency_id"),
                resultSet.getBigDecimal("rate")
        );
    }

    @Override
    public List<ExchangeRates> findAll() {
        List<ExchangeRates> exchangeRatesList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                exchangeRatesList.add(getExchangeRate(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching exchange rates from the database", e);
        }
        return exchangeRatesList;
    }

    @Override
    public ExchangeRates findByCode(String code) {
        return null;
    }

    @Override
    public void save(ExchangeRates entity) {

    }

    @Override
    public void update(ExchangeRates entity) {

    }

    @Override
    public void delete(String code) {
    }
}
