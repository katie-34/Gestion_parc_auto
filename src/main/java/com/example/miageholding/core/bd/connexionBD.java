package com.example.miageholding.core.bd;

import java.sql.*;

public class connexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/auto";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connexion réussie !");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
        return conn;

    }
}





