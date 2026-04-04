package com.apexretail.utilities;

import java.sql.Connection;

import com.apexretail.config.DatabaseConnection;

/**
 * Simple utility to test the database connection.
 * Used during development to verify that the database configuration
 * (db.properties) is correct and that the database server is accessible.
 *
 * @author David
 * @version 1.0.0
 */
public class TestConnection {

    /**
     * Attempts to establish a connection using
     * {@link DatabaseConnection#getConnection()}.
     * Prints a success message if the connection is successful, otherwise prints
     * the stack trace.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Connected to database!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}