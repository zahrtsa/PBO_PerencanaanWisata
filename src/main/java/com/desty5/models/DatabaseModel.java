package com.desty5.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseModel {
    private final Connection connection;

    public DatabaseModel() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver"); // Pastikan driver di-load
        this.connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/desty5_db", "root", ""
        );
        System.out.println("Database connected successfully!"); // Debug log
    }

    public void addName(String name) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO names (name) VALUES (?)")) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Insert successful for: " + name); // Debug log
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to insert name: " + e.getMessage());
        }
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM names")) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
            System.out.println("Retrieved names: " + names); // Debug log
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to retrieve names: " + e.getMessage());
        }
        return names;
    }
}
