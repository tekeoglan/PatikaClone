package com.tekeoglan.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private Connection connect = null;

    public Connection connectDB() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connect = DriverManager.getConnection(Config.CONNECTION_URL);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return this.connect;
    }

    public static Connection getInstance() {
        DBConnector db = new DBConnector();
        return db.connectDB();
    }
}
