package com.desty5.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PembayaranModel {
    private int idPesanan;
    private String namaPemesan;
    private String destinasi;
    private String transportasi;
    private int jumlahOrang;
    private double totalHarga;

    public PembayaranModel(int idPesanan, String namaPemesan, String destinasi, String transportasi, int jumlahOrang, double totalHarga) {
        this.idPesanan = idPesanan;
        this.namaPemesan = namaPemesan;
        this.destinasi = destinasi;
        this.transportasi = transportasi;
        this.jumlahOrang = jumlahOrang;
        this.totalHarga = totalHarga;
    }

    public static PembayaranModel getPembayaranData(Connection connection, int idPesanan) throws SQLException {
        String query = "SELECT p.id, u.nama AS namaPemesan, d.nama AS destinasi, t.nama AS transportasi, " +
                       "p.jumlah_pengunjung AS jumlahOrang, p.total_tagihan AS totalHarga " +
                       "FROM pemesanan p " +
                       "JOIN user u ON p.user_id = u.id " +
                       "JOIN destinasi d ON p.destinasi_id = d.id " +
                       "JOIN transportasi t ON p.transportasi_id = t.id " +
                       "WHERE p.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idPesanan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new PembayaranModel(
                    rs.getInt("id"),
                    rs.getString("namaPemesan"),
                    rs.getString("destinasi"),
                    rs.getString("transportasi"),
                    rs.getInt("jumlahOrang"),
                    rs.getDouble("totalHarga")
                );
            }
        }
        return null;
    }

    public boolean simpanPembayaran(Connection connection, double jumlahPembayaran) {
        if (jumlahPembayaran == this.totalHarga) {
            String query = "INSERT INTO pembayaran (pemesanan_id, jumlah_bayar, status_pembayaran, tanggal_pembayaran) " +
                           "VALUES (?, ?, 'Lunas', CURRENT_DATE)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, this.idPesanan);
                preparedStatement.setDouble(2, jumlahPembayaran);
    
                int rowsInserted = preparedStatement.executeUpdate();
                return rowsInserted > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public int getIdPesanan() { return idPesanan; }
    public String getNamaPemesan() { return namaPemesan; }
    public String getDestinasi() { return destinasi; }
    public String getTransportasi() { return transportasi; }
    public int getJumlahOrang() { return jumlahOrang; }
    public double getTotalHarga() { return totalHarga; }
}
