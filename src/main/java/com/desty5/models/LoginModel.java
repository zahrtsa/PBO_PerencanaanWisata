package com.desty5.models;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    private final DatabaseConnection databaseConnection;

    public LoginModel(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public Boolean verifyLogin(String username, String password) {
        String query = "SELECT password FROM user WHERE username = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPasswordHash = rs.getString("password");
                if (storedPasswordHash != null) {
                    return BCrypt.checkpw(password, storedPasswordHash);
                }
            }
            return false; // Return false if login fails
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to verify login: " + e.getMessage());
            return false;
        }
    }

    public String getUserRole(String username) {
        String query = "SELECT role FROM user WHERE username = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
            return null; // If not found, return null
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal mengambil peran pengguna: " + e.getMessage());
            return null;
        }
    }

    public void insertUser(String nama, String username, String email, String telepon, String password, String role) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt()); // Hash the password directly
        String query = "INSERT INTO user (nama, username, email, telepon, password, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nama);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, telepon);
            stmt.setString(5, hashedPassword); // Store the hashed password
            stmt.setString(6, role);
            stmt.executeUpdate(); // Execute the insert
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to insert user: " + e.getMessage());
        }
    }
}
