package Repository.Impl;

import Entity.ExchangeRates;
import Repository.CurrencyExchangeRepository;
import Utils.CurrencyIdExtractor;
import Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesRepositoryImpl implements CurrencyExchangeRepository<ExchangeRates> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM currencyExchange.ExchangeRates";
    private static final String FIND_BY_CODE_QUERY = "SELECT * FROM currencyExchange.ExchangeRates WHERE base_currency_id = ? AND target_currency_id = ?";
    private static final String SAVE_QUERY = "INSERT INTO currencyExchange.ExchangeRates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
    private static final String DELETE_QUERY = "DELETE FROM currencyExchange.ExchangeRates WHERE base_currency_id = ? AND target_currency_id = ?";
    private static final String UPDATE_QUERY = "UPDATE currencyExchange.ExchangeRates SET rate = ? WHERE base_currency_id = ? AND target_currency_id = ?";

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

        }
        catch (SQLException e) {
            throw new RuntimeException("Error fetching exchange rates from the database", e);
        }
        return exchangeRatesList;
    }

    @Override
    public ExchangeRates findByCode(String exchangeCode) {
        ExchangeRates exchangeRate = null;

        int baseCurrencyId = CurrencyIdExtractor.getCurrencyIdByCode(exchangeCode.substring(0,3));
        int targetCurrencyId = CurrencyIdExtractor.getCurrencyIdByCode(exchangeCode.substring(3));

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODE_QUERY)){

            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = getExchangeRate(resultSet);
                }
            }
        }
        catch (SQLException e) {
            throw new RuntimeException("Error retrieving exchange rate by code", e);
        }
        return exchangeRate;
    }

    @Override
    public void save(ExchangeRates entity) {
        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {

            statement.setInt(1, entity.getBaseCurrencyId());
            statement.setInt(2, entity.getTargetCurrencyId());
            statement.setBigDecimal(3, entity.getRate());

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error saving exchange rate", e);
        }
    }

    @Override
    public void delete(String exchangeCode) {

        int baseCurrencyId = CurrencyIdExtractor.getCurrencyIdByCode(exchangeCode.substring(0,3));
        int targetCurrencyId = CurrencyIdExtractor.getCurrencyIdByCode(exchangeCode.substring(3));

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)){

            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);

            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException("Error deleting exchange rate", e);
        }
    }

    @Override
    public void update(ExchangeRates entity) {

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setBigDecimal(1, entity.getRate());
            statement.setInt(2, entity.getBaseCurrencyId());
            statement.setInt(3, entity.getTargetCurrencyId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating exchange rate", e);
        }
    }
}
