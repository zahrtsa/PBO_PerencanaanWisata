package com.desty5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataDummy {

    public static void tambahDummyData(Connection connection) {
        try {
            // Membuat data dummy untuk tabel destinasi
            tambahDummyDataDestinasi(connection);
            // Membuat data dummy untuk tabel transportasi
            tambahDummyDataTransportasi(connection);
            // Membuat data dummy untuk tabel pemesanan
            tambahDummyDataPemesanan(connection);
            // Membuat data dummy untuk tabel pembayaran
            tambahDummyDataPembayaran(connection);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal dalam memasukkan data dummy: " + e.getMessage());
        }
    }

    private static void tambahDummyDataDestinasi(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO destinasi (gambar, nama, lokasi, deskripsi, harga_destinasi) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            // Insert data dummy for destinations
            stmt.setString(1, "/img/wisata-NTT-labuanbajo.jpg");
            stmt.setString(2, "Pulau Seribu");
            stmt.setString(3, "Jakarta, Indonesia");
            stmt.setString(4, "Pulau yang indah di sebelah utara Jakarta.");
            stmt.setDouble(5, 1500000.0);
            stmt.executeUpdate();

            stmt.setString(1, "/img/wisata-medan-danaulinting.jpg");
            stmt.setString(2, "Bali");
            stmt.setString(3, "Bali, Indonesia");
            stmt.setString(4, "Destinasi wisata paling terkenal di Indonesia.");
            stmt.setDouble(5, 5000000.0);
            stmt.executeUpdate();

            System.out.println("Data dummy untuk tabel destinasi berhasil dimasukkan.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal dalam memasukkan data dummy untuk destinasi: " + e.getMessage());
        }
    }

    private static void tambahDummyDataTransportasi(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO transportasi (nama, jenis, kapasitas, harga_transportasi) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            // Insert data dummy for transportation
            stmt.setString(1, "Bajaj");
            stmt.setString(2, "Public");
            stmt.setInt(3, 5);
            stmt.setDouble(4, 50000.0);
            stmt.executeUpdate();

            stmt.setString(1, "Bus Pariwisata");
            stmt.setString(2, "Private");
            stmt.setInt(3, 40);
            stmt.setDouble(4, 300000.0);
            stmt.executeUpdate();

            System.out.println("Data dummy untuk tabel transportasi berhasil dimasukkan.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal dalam memasukkan data dummy untuk transportasi: " + e.getMessage());
        }
    }

    private static void tambahDummyDataPemesanan(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO pemesanan (user_id, destinasi_id, transportasi_id, jumlah_pengunjung, total_tagihan, status, tanggal_pemesanan) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            // Insert data dummy for booking
            stmt.setInt(1, 1); // Assume user ID 1 for admin
            stmt.setInt(2, 1); // Assume destinasi ID 1
            stmt.setInt(3, 1); // Assume transportasi ID 1
            stmt.setString(4, "5");
            stmt.setDouble(5, 750000.0);
            stmt.setString(6, "Lunas");
            stmt.setDate(7, java.sql.Date.valueOf("2024-12-20"));
            stmt.executeUpdate();

            stmt.setInt(1, 2); // Assume user ID 2 for desty5
            stmt.setInt(2, 2); // Assume destinasi ID 2
            stmt.setInt(3, 2); // Assume transportasi ID 2
            stmt.setString(4, "10");
            stmt.setDouble(5, 2500000.0);
            stmt.setString(6, "Hutang");
            stmt.setDate(7, java.sql.Date.valueOf("2024-12-25"));
            stmt.executeUpdate();

            System.out.println("Data dummy untuk tabel pemesanan berhasil dimasukkan.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal dalam memasukkan data dummy untuk pemesanan: " + e.getMessage());
        }
    }

    private static void tambahDummyDataPembayaran(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO pembayaran (pemesanan_id, jumlah_bayar, status_pembayaran, tanggal_pembayaran) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {
            // Insert data dummy for payments
            stmt.setInt(1, 1); // Assume pemesanan ID 1
            stmt.setDouble(2, 750000.0);
            stmt.setString(3, "Lunas");
            stmt.setDate(4, java.sql.Date.valueOf("2024-12-21"));
            stmt.executeUpdate();

            stmt.setInt(1, 2); // Assume pemesanan ID 2
            stmt.setDouble(2, 500000.0);
            stmt.setString(3, "Hutang");
            stmt.setDate(4, java.sql.Date.valueOf("2024-12-26"));
            stmt.executeUpdate();

            System.out.println("Data dummy untuk tabel pembayaran berhasil dimasukkan.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal dalam memasukkan data dummy untuk pembayaran: " + e.getMessage());
        }
    }
}
