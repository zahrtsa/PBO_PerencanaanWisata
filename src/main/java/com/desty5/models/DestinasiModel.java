package com.desty5.models;

import com.zaxxer.hikari.HikariDataSource;
import org.mindrot.jbcrypt.BCrypt; // Import BCrypt library

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DestinasiModel {
    private int id;
    private String nama;
    private String lokasi;
    private String deskripsi;
    private double hargaDestinasi;
    private String gambar;

    public DestinasiModel(int id, String gambar, String nama, String lokasi, String deskripsi, double hargaDestinasi) {
        this.id = id;
        this.gambar = gambar;
        this.nama = nama;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.hargaDestinasi = hargaDestinasi;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public double getHargaDestinasi() {
        return hargaDestinasi;
    }

    public String getGambar() {
        return gambar;
    }

    // Method to fetch destinasi data from the database
    public static List<DestinasiModel> fetchDestinasiData(Connection connection) {
        List<DestinasiModel> destinasiList = new ArrayList<>();
        String query = "SELECT id, gambar, nama, lokasi, deskripsi, harga_destinasi FROM destinasi";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                DestinasiModel destinasi = new DestinasiModel(
                    resultSet.getInt("id"),
                    resultSet.getString("gambar"),
                    resultSet.getString("nama"),
                    resultSet.getString("lokasi"),
                    resultSet.getString("deskripsi"),
                    resultSet.getDouble("harga_destinasi")
                );
                destinasiList.add(destinasi);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Use proper logging mechanism here
            System.err.println("Gagal untuk mengambil data destinasi: " + e.getMessage());
        }
        return destinasiList;
    }
}
