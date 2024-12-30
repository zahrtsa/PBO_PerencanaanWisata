package com.desty5.controllers.admin.destinasi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

import com.desty5.models.AdminDestinasiModel;
import com.desty5.models.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminViewDestinasiController {

    @FXML
    private TableView<AdminDestinasiModel> destinasiTable;
    @FXML
    private TableColumn<AdminDestinasiModel, Integer> idColumn;
    @FXML
    private TableColumn<AdminDestinasiModel, String> gambarColumn;
    @FXML
    private TableColumn<AdminDestinasiModel, String> namaColumn;
    @FXML
    private TableColumn<AdminDestinasiModel, String> lokasiColumn;
    @FXML
    private TableColumn<AdminDestinasiModel, String> deskripsiColumn;
    @FXML
    private TableColumn<AdminDestinasiModel, Double> hargaColumn;

    @FXML
    public void initialize() {
        gambarColumn.setCellValueFactory(new PropertyValueFactory<>("gambar"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        lokasiColumn.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        deskripsiColumn.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<>("hargaDestinasi"));
        hargaColumn.setCellFactory(column -> new TableCell<AdminDestinasiModel, Double>() {
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
        tampilkanDataKeTabel();
    }

    private void tampilkanDataKeTabel() {
        try (Connection connection = new DatabaseConnection().getConnection()) {
            List<AdminDestinasiModel> destinasiList = AdminDestinasiModel.fetchDestinasiData(connection);

            destinasiTable.getItems().setAll(destinasiList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal mengambil data destinasi.");
        }
    }

    @FXML
    public void tambahDestinasi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/destinasi/AdminTambahDestinasi.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) destinasiTable.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman tambah destinasi.");
        }
    }

    @FXML
    public void editDestinasi() {
        AdminDestinasiModel selectedDestinasi = destinasiTable.getSelectionModel().getSelectedItem();
        if (selectedDestinasi == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Destinasi", "Pilih destinasi yang ingin diubah.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/destinasi/AdminEditDestinasi.fxml"));
            Parent root = loader.load();

            AdminEditDestinasiController controller = loader.getController();
            controller.loadDestinasi(selectedDestinasi);

            Stage stage = (Stage) destinasiTable.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman edit destinasi.");
        }
    }

    @FXML
    public void hapusDestinasi() {
        AdminDestinasiModel selectedDestinasi = destinasiTable.getSelectionModel().getSelectedItem();
        if (selectedDestinasi == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Destinasi", "Pilih destinasi yang ingin dihapus.");
            return;
        }

        try (Connection connection = new DatabaseConnection().getConnection()) {
            AdminDestinasiModel.hapusDestinasi(connection, selectedDestinasi.getId());
            destinasiTable.getItems().remove(selectedDestinasi);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Destinasi berhasil dihapus.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menghapus destinasi.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private Label balikmundur;

    @FXML
    public void mundur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminDashboard.fxml"));
            Stage stage = (Stage) balikmundur.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
