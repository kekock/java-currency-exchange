package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DATABASE_URL = "jdbc:postgresql://localhost:4650/currencyExchange";
    private static final String DATABASE_USER = "admin";
    private static final String DATABASE_PASSWORD = "admin";
    private static final String DATABASE_DRIVER = "org.postgresql.Driver";
    private static final String DRIVER_NOT_FOUND_MESSAGE = "PostgreSQL JDBC Driver not found";

    static {
        try {
            Class.forName(DATABASE_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(DRIVER_NOT_FOUND_MESSAGE, e);
        }
    }

    public static Connection createConnection() throws SQLException {
        return DriverManager
                .getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }
}
