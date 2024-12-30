package com.desty5.controllers.destinasi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.desty5.models.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
    @FXML
    private VBox innerDestinasiContainer;
    @FXML
    private VBox destinasiContainer;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button kembaliButton;
    @FXML
    private Button pesananButton;
    @FXML
    private Button pesananSelesaiButton;

    

    public void initialize() {
        loadData();
    }

    private void loadData() {
        if (gambarImageView == null) {
            System.err.println("gambarImageView is not initialized");
        }
        try (Connection connection = new DatabaseConnection().getConnection()) {
            String query = "SELECT * FROM destinasi";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                List<Destinasi> destinasiList = new ArrayList<>();
    
                while (resultSet.next()) {
                    Destinasi destinasi = new Destinasi();
                    destinasi.setId(resultSet.getInt("id"));
                    destinasi.setImageUrl(resultSet.getString("gambar"));
                    destinasi.setNama(resultSet.getString("nama"));
                    destinasi.setLokasi(resultSet.getString("lokasi"));
                    destinasi.setDeskripsi(resultSet.getString("deskripsi"));
                    destinasi.setHarga(resultSet.getDouble("harga_destinasi"));
    
                    destinasiList.add(destinasi);
                }
    
                if (!destinasiList.isEmpty()) {
                    innerDestinasiContainer.getChildren().clear();
    
                    for (Destinasi destinasi : destinasiList) {
                        VBox destinasiBox = new VBox(10);
                        destinasiBox.setStyle(" -fx-border-color: purple; -fx-border-width: 2px; -fx-alignment: center; -fx-padding: 10;");
    
                        ImageView imageView = new ImageView(new Image(destinasi.getImageUrl()));
                        imageView.setFitWidth(200);
                        imageView.setFitHeight(100);
    
                        Label nameLabel = new Label(destinasi.getNama());
                        Label locationLabel = new Label(destinasi.getLokasi());
                        Label descriptionLabel = new Label(destinasi.getDeskripsi());
                        Label priceLabel = new Label("Rp " + String.format("%,.2f", destinasi.getHarga()));
    
                        destinasiBox.getChildren().addAll(imageView, nameLabel, locationLabel, descriptionLabel, priceLabel);
    
                        destinasiBox.setOnMouseClicked(event -> {
                            System.out.println("Destinasi " + destinasi.getNama() + " clicked!");
                            openNewPage(destinasi);
                        });
    
                        innerDestinasiContainer.getChildren().add(destinasiBox);
                    }
                } else {
                    Label noDestinasiLabel = new Label("Tidak ada destinasi yang ditemukan");
                    innerDestinasiContainer.getChildren().add(noDestinasiLabel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal untuk mengambil data destinasi: " + e.getMessage());
        }
    }


    private void openNewPage(Destinasi destinasi) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destinasi/DetailDestinasi.fxml"));
            Parent detailRoot = loader.load();
    
            DetailDestinasiController detailController = loader.getController();
    
            detailController.loadDetailData(destinasi.getId());
    
            destinasiContainer.getScene().setRoot(detailRoot);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal membuka halaman detail destinasi: " + e.getMessage());
        }
    }

    


    public class Destinasi {
        private int id;
        private String gambar;
        private String nama;
        private String lokasi;
        private String deskripsi;
        private double harga;


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

@FXML
private void handleKembaliButton() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/login.fxml"));
        Parent listRoot = loader.load();

        kembaliButton.getScene().setRoot(listRoot);
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Gagal kembali ke halaman daftar destinasi: " + e.getMessage());
    }
}


@FXML
private void handlePesananButton() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pemesanan/Pesanan.fxml"));
        Parent pemesananRoot = loader.load();

        pesananButton.getScene().setRoot(pemesananRoot);
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Gagal membuka halaman pemesanan: " + e.getMessage());
    }
}

@FXML
private void handlePesananSelesaiButton() {
    try {
FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pemesanan/PesananSelesai.fxml"));
        Parent pesananSelesaiRoot = loader.load();
        pesananSelesaiButton.getScene().setRoot(pesananSelesaiRoot);
    } catch (IOException e) {
        e.printStackTrace();
        System.err.println("Gagal membuka halaman pesanan selesai: " + e.getMessage());
    }
}


}
