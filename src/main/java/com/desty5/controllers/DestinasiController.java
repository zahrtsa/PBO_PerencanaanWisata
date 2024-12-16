package com.desty5.controllers;

import com.desty5.models.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DestinasiController {

    @FXML
    private ImageView gambarImageView;
    @FXML
    private Label namaLabel;
    @FXML
    private Label lokasiLabel;
    @FXML
    private Label deskripsiLabel;
    @FXML
    private Label hargaLabel;

    public void initialize() {
        loadData();
    }

    private void loadData() {
        try (Connection connection = new DatabaseConnection().getConnection()) { // Use DatabaseConnection instance for connection
            String query = "SELECT * FROM destinasi";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Destinasi> destinasiList = new ArrayList<>();
                
                while (resultSet.next()) {
                    Destinasi destinasi = new Destinasi();
                    destinasi.setId(resultSet.getInt("id"));
                    destinasi.setImageUrl(resultSet.getString("gambar")); // Sesuaikan jika gambar disimpan secara lokal
                    destinasi.setNama(resultSet.getString("nama"));
                    destinasi.setLokasi(resultSet.getString("lokasi"));
                    destinasi.setDeskripsi(resultSet.getString("deskripsi"));
                    destinasi.setHarga(resultSet.getDouble("harga_destinasi"));

                    destinasiList.add(destinasi);
                }

                if (!destinasiList.isEmpty()) {
                    // Tampilkan data destinasi pertama
                    Destinasi destinasi = destinasiList.get(0); // Ambil destinasi pertama sebagai contoh
                    gambarImageView.setImage(new Image(destinasi.getImageUrl()));
                    namaLabel.setText(destinasi.getNama());
                    lokasiLabel.setText(destinasi.getLokasi());
                    deskripsiLabel.setText(destinasi.getDeskripsi());
                    hargaLabel.setText("Rp " + destinasi.getHarga());
                } else {
                    // Tampilkan pesan jika tidak ada data
                    namaLabel.setText("Tidak ada destinasi yang ditemukan");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gunakan mekanisme pencatatan yang tepat
            System.err.println("Gagal untuk mengambil data destinasi: " + e.getMessage());
        }
    }

    public class Destinasi {
        private int id;
        private String gambar;
        private String nama;
        private String lokasi;
        private String deskripsi;
        private double harga;

        // Getter dan Setter

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImageUrl() {
            return gambar;
        }

        public void setImageUrl(String gambar) {
            this.gambar = gambar;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getLokasi() {
            return lokasi;
        }

        public void setLokasi(String lokasi) {
            this.lokasi = lokasi;
        }

        public String getDeskripsi() {
            return deskripsi;
        }

        public void setDeskripsi(String deskripsi) {
            this.deskripsi = deskripsi;
        }

        public double getHarga() {
            return harga;
        }

        public void setHarga(double harga) {
            this.harga = harga;
        }
    }
}
