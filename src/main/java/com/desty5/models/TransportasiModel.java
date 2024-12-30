package com.desty5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransportasiModel {
    private final IntegerProperty id;
    private final StringProperty nama;
    private final StringProperty jenis;
    private final IntegerProperty kapasitas;
    private final DoubleProperty hargaTransportasi;

    public TransportasiModel(int id, String nama, String jenis, int kapasitas, double hargaTransportasi) {
        this.id = new SimpleIntegerProperty(id);
        this.nama = new SimpleStringProperty(nama);
        this.jenis = new SimpleStringProperty(jenis);
        this.kapasitas = new SimpleIntegerProperty(kapasitas);
        this.hargaTransportasi = new SimpleDoubleProperty(hargaTransportasi);
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

    public String getNama() {
        return nama.get();
    }

    public void setNama(String nama) {
        this.nama.set(nama);
    }

    public StringProperty namaProperty() {
        return nama;
    }

    public String getJenis() {
        return jenis.get();
    }

    public void setJenis(String jenis) {
        this.jenis.set(jenis);
    }

    public StringProperty jenisProperty() {
        return jenis;
    }

    public int getKapasitas() {
        return kapasitas.get();
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas.set(kapasitas);
    }

    public IntegerProperty kapasitasProperty() {
        return kapasitas;
    }

    public double getHargaTransportasi() {
        return hargaTransportasi.get();
    }

    public void setHargaTransportasi(double hargaTransportasi) {
        this.hargaTransportasi.set(hargaTransportasi);
    }

    public DoubleProperty hargaTransportasiProperty() {
        return hargaTransportasi;
    }

    public static List<TransportasiModel> fetchTransportasiData(Connection connection) {
        List<TransportasiModel> transportasiList = new ArrayList<>();
        String query = "SELECT id, nama, jenis, kapasitas, harga_transportasi FROM transportasi";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                TransportasiModel transportasi = new TransportasiModel(
                        resultSet.getInt("id"),
                        resultSet.getString("nama"),
                        resultSet.getString("jenis"),
                        resultSet.getInt("kapasitas"),
                        resultSet.getDouble("harga_transportasi")
                );
                transportasiList.add(transportasi);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching transportasi data: " + e.getMessage());
            e.printStackTrace();
        }

        return transportasiList;
    }

    public static void tambahTransportasi(Connection connection, String nama, String jenis, int kapasitas, double harga) {
        if (nama == null || nama.isEmpty() || jenis == null || jenis.isEmpty()) {
            System.err.println("Nama dan jenis transportasi tidak boleh kosong.");
            return;
        }

        String query = "INSERT INTO transportasi (nama, jenis, kapasitas, harga_transportasi) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nama);
            statement.setString(2, jenis);
            statement.setInt(3, kapasitas);
            statement.setDouble(4, harga);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding transportasi: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void hapusTransportasi(Connection connection, int transportasiId) {
        try {
            String deletePembayaran = "DELETE FROM pembayaran WHERE pemesanan_id IN (SELECT id FROM pemesanan WHERE transportasi_id = ?)";
            try (PreparedStatement stmtPembayaran = connection.prepareStatement(deletePembayaran)) {
                stmtPembayaran.setInt(1, transportasiId);
                stmtPembayaran.executeUpdate();
            }
    
            String deletePemesanan = "DELETE FROM pemesanan WHERE transportasi_id = ?";
            try (PreparedStatement stmtPemesanan = connection.prepareStatement(deletePemesanan)) {
                stmtPemesanan.setInt(1, transportasiId);
                stmtPemesanan.executeUpdate();
            }
    
            String deleteTransportasi = "DELETE FROM transportasi WHERE id = ?";
            try (PreparedStatement stmtTransportasi = connection.prepareStatement(deleteTransportasi)) {
                stmtTransportasi.setInt(1, transportasiId);
                stmtTransportasi.executeUpdate();
            }
    
        } catch (SQLException e) {
            System.err.println("Error deleting transportasi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public static void updateTransportasi(Connection connection, int id, String nama, String jenis, int kapasitas, double harga) {
        if (nama == null || nama.isEmpty() || jenis == null || jenis.isEmpty()) {
            System.err.println("Nama dan jenis transportasi tidak boleh kosong.");
            return;
        }

        String query = "UPDATE transportasi SET nama = ?, jenis = ?, kapasitas = ?, harga_transportasi = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nama);
            statement.setString(2, jenis);
            statement.setInt(3, kapasitas);
            statement.setDouble(4, harga);
            statement.setInt(5, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating transportasi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
