package com.desty5.controllers.pembayaran;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import com.desty5.models.DatabaseConnection;
import com.desty5.models.PembayaranModel;
import com.desty5.models.PesananModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PembayaranController {

    @FXML
    private Label lblNamaPemesan;

    @FXML
    private Label lblDestinasi;

    @FXML
    private Label lblTransportasi;

    @FXML
    private Label lblJumlahOrang;

    @FXML
    private Label lblTotalHarga;

    @FXML
    private Label alertMessage;

    @FXML
    private TextField inputPembayaran;

    @FXML
    private Button btnBayar;
    
    @FXML
    private PesananModel pesanan;
    @FXML
    private PembayaranModel pembayaranModel;

    public void initializeData(PesananModel pesanan) {
        this.pesanan = pesanan;

        this.pembayaranModel = new PembayaranModel(
            pesanan.getId(),
            pesanan.getUserNama(),
            pesanan.getDestinasiNama(),
            pesanan.getTransportasiNama(),
            pesanan.getJumlahPengunjung(),
            pesanan.getTotalTagihan()
        );

        DecimalFormat decimalFormat = new DecimalFormat("Rp #,###.00");

        lblNamaPemesan.setText(pesanan.getUserNama());
        lblDestinasi.setText(pesanan.getDestinasiNama());
        lblTransportasi.setText(pesanan.getTransportasiNama());
        lblJumlahOrang.setText(String.valueOf(pesanan.getJumlahPengunjung()));
        lblTotalHarga.setText(decimalFormat.format(pesanan.getTotalTagihan()));
    }

    @FXML
    private void handlePayment() {
        String pembayaranInput = inputPembayaran.getText();
        if (pembayaranInput.isEmpty()) {
            alertMessage.setText("Masukkan jumlah pembayaran!");
            return;
        }

        double jumlahPembayaran;
        try {
            jumlahPembayaran = Double.parseDouble(pembayaranInput);
        } catch (NumberFormatException e) {
            alertMessage.setText("Jumlah pembayaran harus berupa angka!");
            return;
        }

        if (jumlahPembayaran < pesanan.getTotalTagihan()) {
            alertMessage.setText("Jumlah pembayaran kurang dari total tagihan!");
            return;
        }

        try (Connection connection = new DatabaseConnection().getConnection()) {

            boolean pembayaranBerhasil = pembayaranModel.simpanPembayaran(connection, jumlahPembayaran);

            if (pembayaranBerhasil) {
                boolean statusUpdated = PesananModel.bayarPesanan(connection, pesanan.getId());
                if (statusUpdated) {
                    showAlert(Alert.AlertType.INFORMATION, "Pembayaran Berhasil", "Pembayaran berhasil! Pesanan telah diperbarui.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Kesalahan", "Pembayaran berhasil, tetapi gagal memperbarui status pesanan!");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Kesalahan", "Terjadi kesalahan saat menyimpan data pembayaran!");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Kesalahan", "Terjadi kesalahan saat menghubungkan ke database: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
        navigateToDestinasiPage();
    }



    private void navigateToDestinasiPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destinasi/Destinasi.fxml"));
            VBox destinasiPage = loader.load(); 
    
            Stage stage = (Stage) btnBayar.getScene().getWindow();
            Scene scene = new Scene(destinasiPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat halaman destinasi.");
        }
    }
}
