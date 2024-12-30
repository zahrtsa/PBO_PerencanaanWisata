package com.desty5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTable {

    public static void setupTables(Connection connection) {
        try {
            createTableUser(connection);
            createTableDestinasi(connection);
            createTableTransportasi(connection);
            createTablePemesanan(connection);
            createTablePembayaran(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error setting up tables: " + e.getMessage());
        }
    }

    private static void createTableUser(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS user (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "nama VARCHAR(255), " +
                     "username VARCHAR(255), " +
                     "email VARCHAR(255), " +
                     "telepon VARCHAR(50), " +
                     "password VARCHAR(255), " +
                     "role VARCHAR(50), " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        executeSQL(connection, sql, "user");
    }

    private static void createTableDestinasi(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS destinasi (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "gambar VARCHAR(255), " +
                     "nama VARCHAR(255), " +
                     "lokasi VARCHAR(255), " +
                     "deskripsi TEXT, " +
                     "harga_destinasi DOUBLE, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        executeSQL(connection, sql, "destinasi");
    }

    private static void createTableTransportasi(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS transportasi (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "nama VARCHAR(255), " +
                     "jenis VARCHAR(255), " +
                     "kapasitas INT, " +
                     "harga_transportasi DOUBLE, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        executeSQL(connection, sql, "transportasi");
    }

    private static void createTablePemesanan(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS pemesanan (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "user_id INT, " +
                     "destinasi_id INT, " +
                     "transportasi_id INT, " +
                     "jumlah_pengunjung VARCHAR(255), " +
                     "total_tagihan DOUBLE, " +
                     "status VARCHAR(255), " +
                     "tanggal_pemesanan DATE, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (user_id) REFERENCES user(id), " +
                     "FOREIGN KEY (destinasi_id) REFERENCES destinasi(id), " +
                     "FOREIGN KEY (transportasi_id) REFERENCES transportasi(id))";
        executeSQL(connection, sql, "pemesanan");
    }

    private static void createTablePembayaran(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS pembayaran (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "pemesanan_id INT, " +
                     "jumlah_bayar DOUBLE, " +
                     "status_pembayaran VARCHAR(255), " +
                     "tanggal_pembayaran DATE, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (pemesanan_id) REFERENCES pemesanan(id))";
        executeSQL(connection, sql, "pembayaran");
    }

    private static void executeSQL(Connection connection, String sql, String tableName) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("Tabel " + tableName + " berhasil dibuat atau sudah ada.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal membuat tabel " + tableName + ": " + e.getMessage());
        }
    }
}