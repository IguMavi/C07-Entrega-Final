package com.supermercado.db;

import java.sql.*;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/DB_Supermercado_Novo";

    private DatabaseConnection() {}

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // NOVO MÉTODO - ADICIONE ISTO!
    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, "Admin", "admin123");
        }
        return connection;
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Desconectado do banco de dados.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao desconectar: " + e.getMessage());
        }
    }
}