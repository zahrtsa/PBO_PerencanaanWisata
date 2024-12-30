package com.desty5.controllers.admin.transportasi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import com.desty5.models.DatabaseConnection;
import com.desty5.models.TransportasiModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminTransportasiViewController {

    @FXML
    private TableView<TransportasiModel> transportasiTable;
    @FXML
    private TableColumn<TransportasiModel, Integer> idColumn;
    @FXML
    private TableColumn<TransportasiModel, String> namaColumn;
    @FXML
    private TableColumn<TransportasiModel, String> jenisColumn;
    @FXML
    private TableColumn<TransportasiModel, Integer> kapasitasColumn;
    @FXML
    private TableColumn<TransportasiModel, Double> hargaColumn;

    @FXML
    public void initialize() {
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        jenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        kapasitasColumn.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));
        hargaColumn.setCellValueFactory(new PropertyValueFactory<>("hargaTransportasi"));
        hargaColumn.setCellFactory(column -> new TableCell<TransportasiModel, Double>() {
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

        Tampildtketabel();
    }

    private void Tampildtketabel() {
        try (Connection connection = new DatabaseConnection().getConnection()) {
            List<TransportasiModel> transportasiList = TransportasiModel.fetchTransportasiData(connection);
            
            System.out.println("Data Transportasi: " + transportasiList);

            transportasiTable.getItems().setAll(transportasiList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal mengambil data transportasi.");
        }
    }

    @FXML
    public void tambahTransportasi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/transportasi/AdminTambahTransportasi.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) transportasiTable.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman tambah transportasi.");
        }
    }

    @FXML
    public void editTransportasi() {
        TransportasiModel selectedTransportasi = transportasiTable.getSelectionModel().getSelectedItem();
        if (selectedTransportasi == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih Transportasi", "Pilih transportasi yang ingin diubah.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/transportasi/AdminEditTransportasi.fxml"));
            Parent root = loader.load();

            AdminTransportasiEditController controller = loader.getController();
            controller.loadTransportasi(selectedTransportasi);

            Stage stage = (Stage) transportasiTable.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman edit transportasi.");
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
    private Label mundurhalaman; 

    @FXML
    public void mundur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/AdminDashboard.fxml"));
            Stage stage = (Stage) mundurhalaman.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }  
    
}
