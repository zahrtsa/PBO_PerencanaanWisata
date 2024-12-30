package com.desty5.controllers.pemesanan;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import com.desty5.models.DatabaseConnection;
import com.desty5.models.PesananModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PesananSelesaiController {
    

    @FXML
    private TableView<PesananModel> pesananSelesaiTable;

    @FXML
    private TableColumn<PesananModel, String> destinasiNamaColumn;

    @FXML
    private TableColumn<PesananModel, String> transportasiNamaColumn;

    @FXML
    private TableColumn<PesananModel, String> userNamaColumn;

    @FXML
    private TableColumn<PesananModel, String> jumlahPengunjungColumn;

    @FXML
    private TableColumn<PesananModel, Double> totalHargaColumn;

    @FXML
    private TableColumn<PesananModel, String> statusColumn;

    @FXML
    private TableColumn<PesananModel, java.sql.Date> tanggalPemesananColumn;

    @FXML
    private Button kembaliButton;

    private ObservableList<PesananModel> pesananSelesaiList = FXCollections.observableArrayList();

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

        loadPesananSelesaiData();
    }

    private void loadPesananSelesaiData() {
        pesananSelesaiList.clear();

        try (Connection connection = new DatabaseConnection().getConnection()) {
            pesananSelesaiList.addAll(PesananModel.fetchPesananSelesaiData(connection));
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data pesanan selesai: " + e.getMessage());
        }

        pesananSelesaiTable.setItems(pesananSelesaiList);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleKembaliButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pemesanan/PesananSelesai.fxml"));
            Parent pesananRoot = loader.load();

            kembaliButton.getScene().setRoot(pesananRoot);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal kembali ke halaman daftar pesanan: " + e.getMessage());
        }
    }

    @FXML
    private Button kembaliDestinasi;

    @FXML
    private void backToDestinasi() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destinasi/Destinasi.fxml"));
            Parent destinasiRoot = loader.load();
            
            kembaliDestinasi.getScene().setRoot(destinasiRoot);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigasi Error", "Gagal kembali ke halaman Destinasi: " + e.getMessage());
        }
    }

}
