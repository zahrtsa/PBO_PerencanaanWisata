package com.desty5.controllers.pemesanan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import com.desty5.controllers.pembayaran.PembayaranController;
import com.desty5.models.DatabaseConnection;
import com.desty5.models.PembayaranModel;
import com.desty5.models.PesananModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PesananController {

    @FXML
    private TableView<PesananModel> pesananTable;

    @FXML
    private TableColumn<PesananModel, String> destinasiNamaColumn;

    @FXML
    private TableColumn<PesananModel, String> transportasiNamaColumn;

    @FXML
    private TableColumn<PesananModel, String> userNamaColumn;

    @FXML
    private TableColumn<PesananModel, Integer> jumlahPengunjungColumn;

    @FXML
    private TableColumn<PesananModel, Double> totalHargaColumn;

    @FXML
    private TableColumn<PesananModel, String> statusColumn;

    @FXML
    private TableColumn<PesananModel, String> tanggalPemesananColumn;

    @FXML
    private Button bayarButton;

    private ObservableList<PesananModel> pesananList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        userNamaColumn.setCellValueFactory(new PropertyValueFactory<>("userNama"));
        destinasiNamaColumn.setCellValueFactory(new PropertyValueFactory<>("destinasiNama"));
        transportasiNamaColumn.setCellValueFactory(new PropertyValueFactory<>("transportasiNama"));
        jumlahPengunjungColumn.setCellValueFactory(new PropertyValueFactory<>("jumlahPengunjung"));

        totalHargaColumn.setCellValueFactory(new PropertyValueFactory<>("totalTagihan"));
        totalHargaColumn.setCellFactory(column -> new TableCell<PesananModel, Double>() {
            private final DecimalFormat decimalFormat = new DecimalFormat("Rp #,###.00");

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(decimalFormat.format(item));
                }
            }
        });

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        tanggalPemesananColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalPemesanan"));

        loadPesananData();
    }

    @FXML
    public void bayarPesanan() {
        PesananModel selectedPesanan = pesananTable.getSelectionModel().getSelectedItem();

        if (selectedPesanan == null) {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih pesanan yang ingin dibayar.");
            return;
        }

        try (Connection connection = new DatabaseConnection().getConnection()) {
            PembayaranModel pembayaranData = PembayaranModel.getPembayaranData(connection, selectedPesanan.getId());
            if (pembayaranData != null) {
                showPembayaranWindow(selectedPesanan);
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat menemukan data pembayaran.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Kesalahan koneksi database: " + e.getMessage());
        }
    }

    private void showPembayaranWindow(PesananModel selectedPesanan) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pembayaran/Pembayaran.fxml"));
            Parent root = loader.load();
    
            PembayaranController pembayaranController = loader.getController();
            pembayaranController.initializeData(selectedPesanan);
    
            Stage stage = (Stage) bayarButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka jendela pembayaran.");
        }
    }
    
    

    public void loadPesananData() {
        pesananList.clear();
        try (Connection connection = new DatabaseConnection().getConnection()) {
            pesananList.addAll(PesananModel.fetchPesananData(connection, "Belum Bayar"));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data pesanan: " + e.getMessage());
        }
        pesananTable.setItems(pesananList);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private Button blkkembali;

    @FXML
    private void Kembalisaja() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destinasi/Destinasi.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) blkkembali.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal kembali ke halaman sebelumnya: " + e.getMessage());
        }
    }
}
