package com.desty5.controllers.admin;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML
    private javafx.scene.text.Text welcomeText;

    @FXML
    private javafx.scene.control.Button transportasiButton;

    @FXML
    private javafx.scene.control.Button destinasiButton;

    @FXML
    public void handleDestinasiClick() {
        loadPage("/fxml/admin/destinasi/AdminViewDestinasi.fxml");
    }

    @FXML
    public void handleTransportasiClick() {
        loadPage("/fxml/admin/transportasi/AdminViewTransportasi.fxml");
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Gagal memuat halaman: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void balikKeLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/login.fxml"));
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
