package com.desty5.controllers.auth;

import com.desty5.models.DatabaseConnection;
import com.desty5.models.LoginModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.SQLException;

public class AuthController {
    private LoginModel loginModel;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisibleField; // TextField to show password in plain text
    @FXML
    private CheckBox showPasswordCheck; // CheckBox to toggle password visibility

    public AuthController() {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection(); // Membuat koneksi ke database
            this.loginModel = new LoginModel(dbConnection); // Inisialisasi LoginModel dengan DatabaseConnection
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal untuk terhubung ke database: " + e.getMessage());
        }
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText(); // Get the password directly

        if (loginModel.verifyLogin(username, password)) {
            System.out.println("Login berhasil untuk user : " + username); // Debug log
            showLoginSuccessAlert();

            String userRole = loginModel.getUserRole(username);
            if (userRole != null) {
                if (userRole.equalsIgnoreCase("admin")) {
                    loadScene("/fxml/Main.fxml");
                } else if (userRole.equalsIgnoreCase("user")) {
                    loadScene("/fxml/destinasi/destinasi.fxml");
                } else {
                    System.err.println("Role user tidak valid.");
                }
            } else {
                System.err.println("Role user tidak ditemukan.");
            }

        } else {
            System.out.println("Login gagal untuk user : " + username); // Debug log
            showLoginFailureAlert(); // Tampilkan alert kegagalan
        }
    }

    private void showLoginSuccessAlert() {
        String username = usernameField.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil Untuk Login!");
        alert.setHeaderText(null);
        alert.setContentText(username + " berhasil login.");
        alert.showAndWait();
    }

    private void showLoginFailureAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Gagal!");
        alert.setHeaderText(null);
        alert.setContentText("Username atau password salah.");
        alert.showAndWait();
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load())); // Ganti scene dengan fxmlFile
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat laman: " + e.getMessage());
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        if (showPasswordCheck.isSelected()) {
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        } else {
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
        }
    }
}
