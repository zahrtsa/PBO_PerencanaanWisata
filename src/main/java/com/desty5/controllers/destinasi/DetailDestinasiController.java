package com.desty5.controllers.destinasi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.desty5.models.DatabaseConnection;
import com.desty5.models.SessionManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class DetailDestinasiController {
    private boolean alertAlreadyShown = false;

    @FXML
    private Button kembaliButton;
    @FXML
    private ImageView detailImageView;
    @FXML
    private Label detailNamaLabel;
    @FXML
    private Label detailLokasiLabel;
    @FXML
    private Label detailDeskripsiLabel;
    @FXML
    private Label detailHargaLabel;
    @FXML
    private Spinner<Integer> jumlahOrangSpinner;
    @FXML
    private ComboBox<String> transportasiComboBox;
    @FXML
    private Label kapasitasLabel;
    @FXML
    private Label hargaDestinasiLabel;
    @FXML
    private Label hargaTransportasiLabel;
    @FXML
    private Label totalHargaLabel;
    @FXML
    private Button pesanButton;

    private double hargaDestinasi;
    private double hargaTransportasi;
    private int kapasitasTransportasi;
    private int destinasiId;

    public void initialize() {
        jumlahOrangSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
        loadTransportasiOptions();

        transportasiComboBox.setOnAction(event -> {
            String selectedTransportasi = transportasiComboBox.getSelectionModel().getSelectedItem();
            if (selectedTransportasi != null) {
                String[] parts = selectedTransportasi.split(" - Rp");
                String transportasiNama = parts[0];
                hargaTransportasi = Double.parseDouble(parts[1].replace(".", ""));
                loadKapasitasTransportasi(transportasiNama);
                updateTotalHarga();
            }
        });

        jumlahOrangSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (kapasitasTransportasi > 0 && newVal > kapasitasTransportasi) {
                showCapacityAlert();
                jumlahOrangSpinner.getValueFactory().setValue(oldVal);
            }
            updateTotalHarga();
        });
    }

    private void loadTransportasiOptions() {
        ObservableList<String> transportasiOptions = FXCollections.observableArrayList();
        try (Connection connection = new DatabaseConnection().getConnection()) {
            String query = "SELECT nama, harga_transportasi FROM transportasi";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    String nama = resultSet.getString("nama");
                    double harga = resultSet.getDouble("harga_transportasi");
                    transportasiOptions.add(nama + " - Rp" + String.format("%,.0f", harga));
                }
                transportasiComboBox.setItems(transportasiOptions);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat data transportasi: " + e.getMessage());
        }
    }

    private void loadKapasitasTransportasi(String transportasiNama) {
        try (Connection connection = new DatabaseConnection().getConnection()) {
            String query = "SELECT kapasitas FROM transportasi WHERE nama = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, transportasiNama);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    kapasitasTransportasi = resultSet.getInt("kapasitas");
                    kapasitasLabel.setText(kapasitasTransportasi + " orang");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat kapasitas transportasi: " + e.getMessage());
        }
    }

    private void showCapacityAlert() {
        if (alertAlreadyShown) {
            return;
        }

        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Peringatan Kapasitas");
            alert.setHeaderText("Jumlah orang melebihi kapasitas!");
            alert.setContentText("Kurangi jumlah orang atau pilih transportasi lain.");
            alert.show();

            PauseTransition delay = new PauseTransition(Duration.seconds(5));
            delay.setOnFinished(e -> alert.close());
            delay.play();
        });

        alertAlreadyShown = true;
    }

    public void loadDetailData(int id) {
        this.destinasiId = id;
        try (Connection connection = new DatabaseConnection().getConnection()) {
            String query = "SELECT * FROM destinasi WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    detailImageView.setImage(new Image(resultSet.getString("gambar")));
                    detailNamaLabel.setText(resultSet.getString("nama"));
                    detailLokasiLabel.setText(resultSet.getString("lokasi"));
                    detailDeskripsiLabel.setText(resultSet.getString("deskripsi"));
                    hargaDestinasi = resultSet.getDouble("harga_destinasi");
                    detailHargaLabel.setText("Rp " + hargaDestinasi);
                    updateTotalHarga();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to load detail data: " + e.getMessage());
        }
    }

    private void updateTotalHarga() {
        int jumlahOrang = jumlahOrangSpinner.getValue();
        double totalHargaDestinasi = hargaDestinasi * jumlahOrang;
        hargaDestinasiLabel.setText("Rp " + String.format("%,.0f", totalHargaDestinasi));
        hargaTransportasiLabel.setText("Rp " + String.format("%,.0f", hargaTransportasi));
        double totalHarga = totalHargaDestinasi + hargaTransportasi;
        totalHargaLabel.setText("Rp " + String.format("%,.0f", totalHarga));
    }

    @FXML
    private void handlePesanButton() {
        try (Connection connection = new DatabaseConnection().getConnection()) {
            String transportasiNama = transportasiComboBox.getSelectionModel().getSelectedItem().split(" - ")[0];
            String transportasiQuery = "SELECT id FROM transportasi WHERE nama = ?";
            int transportasiId = -1;

            try (PreparedStatement preparedStatement = connection.prepareStatement(transportasiQuery)) {
                preparedStatement.setString(1, transportasiNama);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    transportasiId = resultSet.getInt("id");
                }
            }

            int userId = SessionManager.getCurrentUserId();

            String query = "INSERT INTO pemesanan (user_id, destinasi_id, transportasi_id, jumlah_pengunjung, total_tagihan, status, tanggal_pemesanan) " +
                    "VALUES (?, ?, ?, ?, ?, ?, CURRENT_DATE)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setInt(2, destinasiId);
                preparedStatement.setInt(3, transportasiId);
                preparedStatement.setInt(4, jumlahOrangSpinner.getValue());
                preparedStatement.setDouble(5, Double.parseDouble(totalHargaLabel.getText().replace("Rp ", "").replace(".", "")));
                preparedStatement.setString(6, "Belum Bayar");
                preparedStatement.executeUpdate();
                System.out.println("Pesanan berhasil ditambahkan.");
            }

            navigateToPesananPage();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal menambahkan pesanan: " + e.getMessage());
        }
    }

    private void navigateToPesananPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pemesanan/Pesanan.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) pesanButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal membuka halaman Pesanan: " + e.getMessage());
        }
    }

    @FXML
    private void handleKembaliButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destinasi/Destinasi.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) kembaliButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal kembali ke halaman sebelumnya: " + e.getMessage());
        }
    }
}
