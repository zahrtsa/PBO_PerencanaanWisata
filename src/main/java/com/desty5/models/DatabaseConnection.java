package com.desty5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(2000);


        this.dataSource = new HikariDataSource(config);
        System.out.println("Berhasil terhubung ke database dengan HikariCP!");

        try (Connection connection = getConnection()) {
            if (!isDatabaseExist(connection, "desty5_db")) {
                System.err.println("Database 'desty5_db' tidak ada.");
                return;
            }

            if (!isTableExist(connection, "user")) {
                createUserTableAndDefaultUser(connection);
            }

            CreateTable.setupTables(connection);
            if (!isAllDummyDataExists(connection)) {
                DataDummy.tambahDummyData(connection);
            } else {
                System.out.println("Data dummy sudah ada di semua tabel, tidak perlu ditambahkan lagi.");
            }
            
            
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

    private boolean isAllDummyDataExists(Connection connection) throws SQLException {
        return isDummyDataInDestinasiExists(connection) &&
               isDummyDataInTransportasiExists(connection) &&
               isDummyDataInPemesananExists(connection) &&
               isDummyDataInPembayaranExists(connection);
    }
    
    private boolean isDummyDataInDestinasiExists(Connection connection) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM destinasi WHERE nama = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkSQL)) {
            stmt.setString(1, "Labuan Bajo");
            try (var resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
    
    private boolean isDummyDataInTransportasiExists(Connection connection) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM transportasi WHERE nama = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkSQL)) {
            stmt.setString(1, "Bus Pariwisata");
            try (var resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
    
    private boolean isDummyDataInPemesananExists(Connection connection) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM pemesanan WHERE user_id = ? AND destinasi_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkSQL)) {
            stmt.setInt(1, 2);
            stmt.setInt(2, 4);
            try (var resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
    
    private boolean isDummyDataInPembayaranExists(Connection connection) throws SQLException {
        String checkSQL = "SELECT COUNT(*) FROM pembayaran WHERE pemesanan_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(checkSQL)) {
            stmt.setInt(1, 1);
            try (var resultSet = stmt.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
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

            connection.setAutoCommit(false);
            stmtCreateTable.executeUpdate();
            System.out.println("Berhasil membuat tabel user.");

            String saltAdmin = BCrypt.gensalt();
            String hashedPasswordAdmin = BCrypt.hashpw("@root!AW0mn", saltAdmin);
            stmtInsertUser.setString(1, "Admin Default");
            stmtInsertUser.setString(2, "admin");
            stmtInsertUser.setString(3, "admin@example.com");
            stmtInsertUser.setString(4, "081234567890");
            stmtInsertUser.setString(5, hashedPasswordAdmin);
            stmtInsertUser.setString(6, "admin");
            stmtInsertUser.executeUpdate();
            System.out.println("Admin user berhasil dibuat!");

            String saltDesty5 = BCrypt.gensalt();
            String hashedPasswordDesty5 = BCrypt.hashpw("SecurePass!123", saltDesty5);
            stmtInsertUser.setString(1, "desty5");
            stmtInsertUser.setString(2, "desty5");
            stmtInsertUser.setString(3, "desty5@example.com");
            stmtInsertUser.setString(4, "081234567890");
            stmtInsertUser.setString(5, hashedPasswordDesty5);
            stmtInsertUser.setString(6, "user");
            stmtInsertUser.executeUpdate();
            System.out.println("User 'desty5' berhasil terbuat!");

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            System.err.println("Gagal untuk membuat tabel user dan menambahkan data : " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
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
