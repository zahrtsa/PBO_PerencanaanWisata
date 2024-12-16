package com.desty5.models;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mindrot.jbcrypt.BCrypt; // Import BCrypt library

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    private final HikariDataSource dataSource;

    public DatabaseConnection() throws SQLException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/desty5_db");
        config.setUsername("root");
        config.setPassword("");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
        System.out.println("Berhasil terhubung ke database dengan HikariCP!"); // Debug log

        try (Connection connection = getConnection()) {
            // Check if database exists
            if (!isDatabaseExist(connection, "desty5_db")) {
                System.err.println("Database 'desty5_db' tidak ada.");
                return;
            }

            // Check if user table exists
            if (!isTableExist(connection, "user")) {
                // Table does not exist, create user table and insert default users
                createUserTableAndDefaultUser(connection);
            }

            // Setup additional tables
            CreateTable.setupTables(connection);
            DataDummy.tambahDummyData(connection);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private boolean isDatabaseExist(Connection connection, String dbName) {
        try (ResultSet resultSet = connection.getMetaData().getCatalogs()) {
            while (resultSet.next()) {
                if (resultSet.getString(1).equalsIgnoreCase(dbName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isTableExist(Connection connection, String tableName) {
        try (ResultSet resultSet = connection.getMetaData().getTables("desty5_db", null, tableName, null)) {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal untuk check tabel: " + e.getMessage());
            return false;
        }
    }

    private void createUserTableAndDefaultUser(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS user (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nama VARCHAR(255) NOT NULL, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "email VARCHAR(255) NOT NULL, " +
                "telepon VARCHAR(15) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "role VARCHAR(50) NOT NULL, " +
                "create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        String insertUserSQL = "INSERT INTO user (nama, username, email, telepon, password, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmtCreateTable = connection.prepareStatement(createTableSQL);
             PreparedStatement stmtInsertUser = connection.prepareStatement(insertUserSQL)) {

            connection.setAutoCommit(false); // Start transaction
            stmtCreateTable.executeUpdate();
            System.out.println("Berhasil membuat tabel user.");

            // Insert default admin user
            String saltAdmin = BCrypt.gensalt(); // Generate salt for admin
            String hashedPasswordAdmin = BCrypt.hashpw("@root!AW0mn", saltAdmin); // Hash the password with the salt
            stmtInsertUser.setString(1, "Admin Default");
            stmtInsertUser.setString(2, "admin");
            stmtInsertUser.setString(3, "admin@example.com");
            stmtInsertUser.setString(4, "081234567890");
            stmtInsertUser.setString(5, hashedPasswordAdmin);
            stmtInsertUser.setString(6, "admin");
            stmtInsertUser.executeUpdate();
            System.out.println("Admin user berhasil dibuat!");

            // Insert user 'desty5'
            String saltDesty5 = BCrypt.gensalt(); // Generate salt for desty5
            String hashedPasswordDesty5 = BCrypt.hashpw("SecurePass!123", saltDesty5); // Hash the password with the salt
            stmtInsertUser.setString(1, "desty5");
            stmtInsertUser.setString(2, "desty5");
            stmtInsertUser.setString(3, "desty5@example.com");
            stmtInsertUser.setString(4, "081234567890");
            stmtInsertUser.setString(5, hashedPasswordDesty5);
            stmtInsertUser.setString(6, "user");
            stmtInsertUser.executeUpdate();
            System.out.println("User 'desty5' berhasil terbuat!");

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction in case of error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            System.err.println("Gagal untuk membuat tabel user dan menambahkan data : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true); // Reset auto-commit mode
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Gagal untuk reset auto-commit mode: " + e.getMessage());
            }
        }
    }

    public void close() {
        if (dataSource != null) {
            dataSource.close();
            System.out.println("HikariCP DataSource closed.");
        }
    }
}
