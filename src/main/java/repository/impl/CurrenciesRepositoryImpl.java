package repository.impl;

import entity.Currencies;
import repository.CurrencyExchangeRepository;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesRepositoryImpl implements CurrencyExchangeRepository<Currencies> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM currencyExchange.Currencies";
    private static final String FIND_BY_CODE_QUERY = "SELECT * FROM currencyExchange.Currencies WHERE code = ?";
    private static final String SAVE_QUERY = "INSERT INTO currencyExchange.Currencies (code, full_name, sign) VALUES (?, ?, ?) "
            + "RETURNING id, code, full_name, sign";
    private static final String DELETE_QUERY = "DELETE FROM currencyExchange.Currencies WHERE code = ?";
    private static final String UPDATE_QUERY = "UPDATE currencyExchange.Currencies SET full_name = ?, sign = ? WHERE code = ? "
            + "RETURNING id, code, full_name, sign";

    @Override
    public List<Currencies> findAll() {
        List<Currencies> currenciesList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);

             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                currenciesList.add(getCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching currencies from the database", e);
        }
        return currenciesList;
    }

    @Override
    public Currencies findByCode(String code) {
        Currencies currency = null;

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODE_QUERY)) {

            statement.setString(1, code);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = getCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving currency by code", e);
        }

        return currency;
    }

    @Override
    public Currencies save(Currencies entity) {
        Currencies savedCurrency = null;

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    savedCurrency = getCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving currency", e);
        }

        return savedCurrency;
    }

    @Override
    public void delete(String code) {

        try (Connection connection = DatabaseConnection.createConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)){

            statement.setString(1, code);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting currency", e);
        }
    }

    @Override
    public Currencies update(Currencies entity) {
        Currencies updatedCurrency = null;

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, entity.getFullName());
            statement.setString(2, entity.getSign());
            statement.setString(3, entity.getCode());

            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    updatedCurrency = getCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error updating currency", e);
        }

        return updatedCurrency;
    }

    private static Currencies getCurrency(ResultSet resultSet) throws SQLException {
        return new Currencies(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}