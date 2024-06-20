package com.cafeteria.server.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "/config.properties";
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try (InputStream input = DatabaseConnection.class.getResourceAsStream(CONFIG_FILE)) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
            } else {
                prop.load(input);
                URL = prop.getProperty("db.url");
                USER = prop.getProperty("db.user");
                PASSWORD = prop.getProperty("db.password");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
