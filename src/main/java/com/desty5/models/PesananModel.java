package com.desty5.models;

import java.sql.Connection;
import java.sql.Date;
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

public class PesananModel {
    private IntegerProperty id;
    private StringProperty userNama;
    private StringProperty destinasiNama;
    private StringProperty transportasiNama;
    private IntegerProperty jumlahPengunjung;
    private DoubleProperty totalTagihan;
    private StringProperty status;
    private ObjectProperty<Date> tanggalPemesanan;

    public PesananModel(int id, String userNama, String destinasiNama, String transportasiNama, 
                        int jumlahPengunjung, double totalTagihan, String status, Date tanggalPemesanan) {
        this.id = new SimpleIntegerProperty(id);
        this.userNama = new SimpleStringProperty(userNama);
        this.destinasiNama = new SimpleStringProperty(destinasiNama);
        this.transportasiNama = new SimpleStringProperty(transportasiNama);
        this.jumlahPengunjung = new SimpleIntegerProperty(jumlahPengunjung);
        this.totalTagihan = new SimpleDoubleProperty(totalTagihan);
        this.status = new SimpleStringProperty(status);
        this.tanggalPemesanan = new SimpleObjectProperty<>(tanggalPemesanan);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getUserNama() {
        return userNama.get();
    }

    public void setUserNama(String userNama) {
        this.userNama.set(userNama);
    }

    public String getDestinasiNama() {
        return destinasiNama.get();
    }

    public void setDestinasiNama(String destinasiNama) {
        this.destinasiNama.set(destinasiNama);
    }

    public String getTransportasiNama() {
        return transportasiNama.get();
    }

    public void setTransportasiNama(String transportasiNama) {
        this.transportasiNama.set(transportasiNama);
    }

    public int getJumlahPengunjung() {
        return jumlahPengunjung.get();
    }

    public void setJumlahPengunjung(int jumlahPengunjung) {
        this.jumlahPengunjung.set(jumlahPengunjung);
    }

    public double getTotalTagihan() {
        return totalTagihan.get();
    }

    public void setTotalTagihan(double totalTagihan) {
        this.totalTagihan.set(totalTagihan);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public Date getTanggalPemesanan() {
        return tanggalPemesanan.get();
    }

    public void setTanggalPemesanan(Date tanggalPemesanan) {
        this.tanggalPemesanan.set(tanggalPemesanan);
    }

    public static List<PesananModel> fetchPesananData(Connection connection, String statusFilter) {
        List<PesananModel> pesananList = new ArrayList<>();
        String query = "SELECT p.id, u.nama AS user_nama, d.nama AS destinasi_nama, t.nama AS transportasi_nama, " +
                       "p.jumlah_pengunjung, p.total_tagihan, p.status, p.tanggal_pemesanan " +
                       "FROM pemesanan p " +
                       "JOIN user u ON p.user_id = u.id " +
                       "JOIN destinasi d ON p.destinasi_id = d.id " + 
                       "JOIN transportasi t ON p.transportasi_id = t.id " +
                       "WHERE p.status = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, statusFilter);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    PesananModel pesanan = new PesananModel(
                        resultSet.getInt("id"),
                        resultSet.getString("user_nama"),
                        resultSet.getString("destinasi_nama"),
                        resultSet.getString("transportasi_nama"),
                        resultSet.getInt("jumlah_pengunjung"),
                        resultSet.getDouble("total_tagihan"),
                        resultSet.getString("status"),
                        resultSet.getDate("tanggal_pemesanan")
                    );
                    pesananList.add(pesanan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal mengambil data pesanan: " + e.getMessage());
        }
        return pesananList;
    }

    public static boolean bayarPesanan(Connection connection, int pesananId) {
        String queryUpdateStatus = "UPDATE pemesanan SET status = 'Sudah Bayar' WHERE id = ?";
    
        try (PreparedStatement preparedStatementUpdate = connection.prepareStatement(queryUpdateStatus)) {
            preparedStatementUpdate.setInt(1, pesananId);
            
            int rowsUpdated = preparedStatementUpdate.executeUpdate();
    
            if (rowsUpdated > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<PesananModel> fetchPesananSelesaiData(Connection connection) {
        List<PesananModel> pesananSelesaiList = new ArrayList<>();
        String query = "SELECT p.id, u.nama AS user_nama, d.nama AS destinasi_nama, t.nama AS transportasi_nama, " +
                       "p.jumlah_pengunjung, p.total_tagihan, p.status, p.tanggal_pemesanan " +
                       "FROM pemesanan p " +
                       "JOIN user u ON p.user_id = u.id " + 
                       "JOIN destinasi d ON p.destinasi_id = d.id " + 
                       "JOIN transportasi t ON p.transportasi_id = t.id " + 
                       "WHERE p.status = 'Sudah Bayar'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                PesananModel pesanan = new PesananModel(
                    resultSet.getInt("id"),
                    resultSet.getString("user_nama"),
                    resultSet.getString("destinasi_nama"),
                    resultSet.getString("transportasi_nama"),
                    resultSet.getInt("jumlah_pengunjung"),
                    resultSet.getDouble("total_tagihan"),
                    resultSet.getString("status"),
                    resultSet.getDate("tanggal_pemesanan")
                );
                pesananSelesaiList.add(pesanan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal mengambil data pesanan selesai: " + e.getMessage());
        }
        return pesananSelesaiList;
    }
}
