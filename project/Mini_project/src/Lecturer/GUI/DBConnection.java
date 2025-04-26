package Lecturer.GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/TecMIS_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (conn != null) {
            System.out.println("✅ Database connection successful!");
        } else {
            System.out.println("❌ Database connection failed!");
        }
        return conn;
    }

    public static void main(String[] args) {
        try {
            getConnection();
        } catch (SQLException e) {
            System.err.println("Database connection error:");
            e.printStackTrace();
        }
    }
}