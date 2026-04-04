package com.apexretail.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing database connections.
 * Loads connection parameters from a properties file located at
 * {@code /db.properties} in the classpath.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>{@code
 * try (Connection conn = DatabaseConnection.getConnection()) {
 *     // use connection
 * }
 * }</pre>
 *
 * @author David
 * @version 1.0.0
 */
public class DatabaseConnection {

    /** Holds the database configuration properties loaded from db.properties. */
    private static final Properties properties = new Properties();

    // Static initializer: loads properties once when the class is loaded
    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Could not find db.properties");
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    /**
     * Establishes and returns a connection to the database using the loaded
     * configuration properties.
     *
     * @return a Connection object to the database
     * @throws SQLException if a database access error occurs or the URL is null
     */
    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }
}