package Repository.Impl;

import Entity.Currencies;
import Repository.CurrencyExchangeRepository;
import Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesRepository implements CurrencyExchangeRepository<Currencies> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM currencyExchange.Currencies";
    private static final String FIND_BY_CODE_QUERY = "SELECT * FROM currencyExchange.Currencies WHERE code = ?";
    private static final String SAVE_QUERY = "INSERT INTO currencyExchange.Currencies (code, full_name, sign) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE currencyExchange.Currencies SET full_name = ?, sign = ? WHERE code = ?";

    private static Currencies getCurrency(ResultSet resultSet) throws SQLException {
        return new Currencies(
                resultSet.getInt("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }

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
    public void save(Currencies currency) {

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving currency", e);
        }
    }

    @Override
    public void delete(String code) {
        final String query = "DELETE FROM currencyExchange.Currencies WHERE code = ?";

        try (Connection connection = DatabaseConnection.createConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setString(1, code);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Currencies currency) {

        try (Connection connection = DatabaseConnection.createConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            statement.setString(1, currency.getFullName());
            statement.setString(2, currency.getSign());
            statement.setString(3, currency.getCode());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating currency", e);
        }
    }
}