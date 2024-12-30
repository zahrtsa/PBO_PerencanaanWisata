package com.desty5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataDummy {

    public static void tambahDummyData(Connection connection) {
        try {
            tambahDummyDataDestinasi(connection);
            tambahDummyDataTransportasi(connection);
            tambahDummyDataPemesanan(connection);
            tambahDummyDataPembayaran(connection);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal dalam memasukkan data dummy: " + e.getMessage());
        }
    }

    private static void tambahDummyDataDestinasi(Connection connection) throws SQLException {
        String insertSQL = "INSERT INTO destinasi (gambar, nama, lokasi, deskripsi, harga_destinasi) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertSQL)) {

            stmt.setString(1, "/img/LabuanBajo.jpg");
            stmt.setString(2, "Labuan Bajo");
            stmt.setString(3, "Manggarai, NTT");
            stmt.setString(4, "Surga tropis di NTT, gerbang menuju Taman Nasional Komodo.");
            stmt.setDouble(5, 1800000.0);
            stmt.executeUpdate();

            stmt.setString(1, "/img/DanauLinting.jpg");
            stmt.setString(2, "Danau Linting");
            stmt.setString(3, "Deli Serdang, Sumatera Utara");
            stmt.setString(4, "Danau vulkanik biru kehijauan di Sumatera Utara.");
            stmt.setDouble(5, 750000.0);
            stmt.executeUpdate();

            stmt.setString(1, "/img/Bromo.jpg");
            stmt.setString(2, "Bromo Tengger Semeru");
            stmt.setString(3, "Probolinggo, Jawa Timur");
            stmt.setString(4, "Gunung dengan panorama matahari terbit dan lautan pasir di Jawa Timur.");
            stmt.setDouble(5, 2500000.0);
            stmt.executeUpdate();

            stmt.setString(1, "/img/GreenCanyon.jpg");
            stmt.setString(2, "Green Canyon");
            stmt.setString(3, "Pangandaran, Jawa Barat");
            stmt.setString(4, "Ngarai hijau memukau di Pangandaran, cocok untuk body rafting.");
            stmt.setDouble(5, 1200000.0);
            stmt.executeUpdate();

            stmt.setString(1, "/img/KawahIjen.jpg");
            stmt.setString(2, "Kawah Ijen");
            stmt.setString(3, "Banyuwangi, Jawa Timur");
            stmt.setString(4, "Kawah dengan fenomena \"blue fire\" di Banyuwangi, Jawa Timur.");
            stmt.setDouble(5, 2000000.0);
            stmt.executeUpdate();

            stmt.setString(1, "/img/GiliTrawangan.jpg");
            stmt.setString(2, "Gili Trawangan");
            stmt.setString(3, "Lombok, NTB");
            stmt.setString(4, "Pulau dengan pantai bersih dan suasana santai di Lombok.");
            stmt.setDouble(5, 3000000.0);
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
            stmt.setString(1, "Bus Pariwisata");
            stmt.setString(2, "Public");
            stmt.setInt(3, 5);
            stmt.setDouble(4, 150000.0);
            stmt.executeUpdate();

            stmt.setString(1, "Minibus");
            stmt.setString(2, "Public");
            stmt.setInt(3, 40);
            stmt.setDouble(4, 100000.0);
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
            stmt.setInt(1, 2);
            stmt.setInt(2, 4);
            stmt.setInt(3, 1);
            stmt.setString(4, "5");
            stmt.setDouble(5, 6150000.0);
            stmt.setString(6, "Sudah Bayar");
            stmt.setDate(7, java.sql.Date.valueOf("2024-12-20"));
            stmt.executeUpdate();

            stmt.setInt(1, 2);
            stmt.setInt(2, 2);
            stmt.setInt(3, 2);
            stmt.setString(4, "10");
            stmt.setDouble(5, 7600000.0);
            stmt.setString(6, "Sudah Bayar");
            stmt.setDate(7, java.sql.Date.valueOf("2024-12-25"));
            stmt.executeUpdate();

            stmt.setInt(1, 2);
            stmt.setInt(2, 3);
            stmt.setInt(3, 1);
            stmt.setString(4, "2");
            stmt.setDouble(5, 5150000.0);
            stmt.setString(6, "Belum Bayar");
            stmt.setDate(7, java.sql.Date.valueOf("2024-12-29"));
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
            stmt.setInt(1, 1);
            stmt.setDouble(2, 6150000.0);
            stmt.setString(3, "Lunas");
            stmt.setDate(4, java.sql.Date.valueOf("2024-12-21"));
            stmt.executeUpdate();

            stmt.setInt(1, 2);
            stmt.setDouble(2, 7600000.0);
            stmt.setString(3, "Lunas");
            stmt.setDate(4, java.sql.Date.valueOf("2024-12-26"));
            stmt.executeUpdate();

            System.out.println("Data dummy untuk tabel pembayaran berhasil dimasukkan.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal dalam memasukkan data dummy untuk pembayaran: " + e.getMessage());
        }
    }
}
