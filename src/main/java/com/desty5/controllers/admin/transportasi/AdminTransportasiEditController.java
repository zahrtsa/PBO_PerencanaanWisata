package com.desty5.controllers.admin.transportasi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import com.desty5.models.DatabaseConnection;
import com.desty5.models.TransportasiModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminTransportasiEditController {

    @FXML
    private TextField namaField;
    @FXML
    private TextField jenisField;
    @FXML
    private TextField kapasitasField;
    @FXML
    private TextField hargaField;

    private TransportasiModel selectedTransportasi;

    public void loadTransportasi(TransportasiModel transportasi) {
        this.selectedTransportasi = transportasi;
        namaField.setText(transportasi.getNama());
        jenisField.setText(transportasi.getJenis());
        kapasitasField.setText(String.valueOf(transportasi.getKapasitas()));
        hargaField.setText(String.valueOf(transportasi.getHargaTransportasi()));
    }

    @FXML
    public void updateTransportasi() {
        String nama = namaField.getText().trim();
        String jenis = jenisField.getText().trim();
        String kapasitasStr = kapasitasField.getText().trim();
        String hargaStr = hargaField.getText().trim();

        if (nama.isEmpty() || jenis.isEmpty() || kapasitasStr.isEmpty() || hargaStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Semua field harus diisi.");
            return;
        }

        try {
            int kapasitas = Integer.parseInt(kapasitasStr);
            double harga = Double.parseDouble(hargaStr);

            try (Connection connection = new DatabaseConnection().getConnection()) {
                TransportasiModel.updateTransportasi(connection, selectedTransportasi.getId(), nama, jenis, kapasitas, harga);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data transportasi berhasil diperbarui!");
                navigateToTransportasiController();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memperbarui data transportasi.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Kapasitas harus berupa angka dan Harga harus berupa angka desimal.");
        }
    }

    @FXML
    public void deleteTransportasi() {
        if (selectedTransportasi == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Tidak ada data transportasi yang dipilih.");
            return;
        }

        try (Connection connection = new DatabaseConnection().getConnection()) {
            TransportasiModel.hapusTransportasi(connection, selectedTransportasi.getId());
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data transportasi berhasil dihapus!");
            navigateToTransportasiController();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menghapus data transportasi.");
        }
    }

    @FXML
    public void navigateToTransportasiController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/transportasi/AdminViewTransportasi.fxml"));
            Parent root = loader.load();
    
            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat halaman transportasi.");
        }
    }
    

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
