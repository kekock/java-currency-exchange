package Repository.Impl;

import Entity.Currencies;
import Repository.CurrencyRepository;
import Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesRepository implements CurrencyRepository {

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
        final String query = "SELECT * FROM currencyExchange.Currencies"; // проверьте корректность схемы
        List<Currencies> currenciesList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
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
    public Optional<Currencies> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public void save(Object entity) {
    }

    @Override
    public void update(Object entity) {
    }
}
