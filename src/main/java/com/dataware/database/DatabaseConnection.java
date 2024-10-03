package com.dataware.database;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;


    private void reconnect() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find application.properties");
            }
            properties.load(input);

            String URL = properties.getProperty("db.url");
            String USER = properties.getProperty("db.username");
            String PASSWORD = properties.getProperty("db.password");

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("connected");
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to connect to the database", ex);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Re-establish the connection if it's closed
                System.out.println("Connection was closed, re-establishing...");
                reconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
