package com.desty5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class AdminDestinasiModel {
    private final IntegerProperty id;
    private final StringProperty gambar;
    private final StringProperty nama;
    private final StringProperty lokasi;
    private final StringProperty deskripsi;
    private final DoubleProperty hargaDestinasi;
    private final ObjectProperty<java.sql.Timestamp> createdAt;

    private static final String IMAGE_DIRECTORY = "resources/img/";

    public AdminDestinasiModel(int id, String gambar, String nama, String lokasi, String deskripsi, double hargaDestinasi, java.sql.Timestamp createdAt) {
        this.id = new SimpleIntegerProperty(id);
        this.gambar = new SimpleStringProperty(gambar);
        this.nama = new SimpleStringProperty(nama);
        this.lokasi = new SimpleStringProperty(lokasi);
        this.deskripsi = new SimpleStringProperty(deskripsi);
        this.hargaDestinasi = new SimpleDoubleProperty(hargaDestinasi);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getGambar() {
        return gambar.get();
    }

    public void setGambar(String gambar) {
        this.gambar.set(gambar);
    }

    public StringProperty gambarProperty() {
        return gambar;
    }

    public String getNama() {
        return nama.get();
    }

    public void setNama(String nama) {
        this.nama.set(nama);
    }

    public StringProperty namaProperty() {
        return nama;
    }

    public String getLokasi() {
        return lokasi.get();
    }

    public void setLokasi(String lokasi) {
        this.lokasi.set(lokasi);
    }

    public StringProperty lokasiProperty() {
        return lokasi;
    }

    public String getDeskripsi() {
        return deskripsi.get();
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi.set(deskripsi);
    }

    public StringProperty deskripsiProperty() {
        return deskripsi;
    }

    public double getHargaDestinasi() {
        return hargaDestinasi.get();
    }

    public void setHargaDestinasi(double hargaDestinasi) {
        this.hargaDestinasi.set(hargaDestinasi);
    }

    public DoubleProperty hargaDestinasiProperty() {
        return hargaDestinasi;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt.set(createdAt);
    }

    public ObjectProperty<java.sql.Timestamp> createdAtProperty() {
        return createdAt;
    }

    public static List<AdminDestinasiModel> fetchDestinasiData(Connection connection) {
        List<AdminDestinasiModel> destinasiList = new ArrayList<>();
        String query = "SELECT id, gambar, nama, lokasi, deskripsi, harga_destinasi, created_at FROM destinasi";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                AdminDestinasiModel destinasi = new AdminDestinasiModel(
                        resultSet.getInt("id"),
                        resultSet.getString("gambar"),
                        resultSet.getString("nama"),
                        resultSet.getString("lokasi"),
                        resultSet.getString("deskripsi"),
                        resultSet.getDouble("harga_destinasi"),
                        resultSet.getTimestamp("created_at")
                );
                destinasiList.add(destinasi);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching destinasi data: " + e.getMessage());
            e.printStackTrace();
        }

        return destinasiList;
    }

    public static void tambahDestinasi(Connection connection, String gambarFilePath, String nama, String lokasi, String deskripsi, double harga) {
        if (nama == null || nama.isEmpty() || lokasi == null || lokasi.isEmpty()) {
            System.err.println("Nama dan lokasi destinasi tidak boleh kosong.");
            return;
        }

        String fileName = gambarFilePath;

        String query = "INSERT INTO destinasi (gambar, nama, lokasi, deskripsi, harga_destinasi) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fileName);
            statement.setString(2, nama);
            statement.setString(3, lokasi);
            statement.setString(4, deskripsi);
            statement.setDouble(5, harga);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding destinasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void hapusDestinasi(Connection connection, int destinasiId) {
        try {
            String deletePembayaran = "DELETE FROM pembayaran WHERE pemesanan_id IN (SELECT id FROM pemesanan WHERE destinasi_id = ?)";
            try (PreparedStatement stmtPembayaran = connection.prepareStatement(deletePembayaran)) {
                stmtPembayaran.setInt(1, destinasiId);
                stmtPembayaran.executeUpdate();
            }
    
            String deletePemesanan = "DELETE FROM pemesanan WHERE destinasi_id = ?";
            try (PreparedStatement stmtPemesanan = connection.prepareStatement(deletePemesanan)) {
                stmtPemesanan.setInt(1, destinasiId);
                stmtPemesanan.executeUpdate();
            }
    
            String deleteDestinasi = "DELETE FROM destinasi WHERE id = ?";
            try (PreparedStatement stmtDestinasi = connection.prepareStatement(deleteDestinasi)) {
                stmtDestinasi.setInt(1, destinasiId);
                stmtDestinasi.executeUpdate();
            }
    
        } catch (SQLException e) {
            System.err.println("Error deleting destinasi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    

    public static void updateDestinasi(Connection connection, int id, String gambarFilePath, String nama, String lokasi, String deskripsi, double harga) {
        if (nama == null || nama.isEmpty() || lokasi == null || lokasi.isEmpty()) {
            System.err.println("Nama dan lokasi destinasi tidak boleh kosong.");
            return;
        }

        String fileName = gambarFilePath != null && !gambarFilePath.isEmpty() ? gambarFilePath : null;

        String query = "UPDATE destinasi SET gambar = ?, nama = ?, lokasi = ?, deskripsi = ?, harga_destinasi = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fileName);
            statement.setString(2, nama);
            statement.setString(3, lokasi);
            statement.setString(4, deskripsi);
            statement.setDouble(5, harga);
            statement.setInt(6, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating destinasi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
