package com.desty5.controllers.auth;

import com.desty5.models.DatabaseConnection;
import com.desty5.models.LoginModel;
import com.desty5.models.SessionManager;
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

public class AturLogin {
    private LoginModel loginModel;

    private String username;
    private String password;

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisibleField;
    @FXML
    private CheckBox showPasswordCheck;

    public AturLogin() {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            this.loginModel = new LoginModel(dbConnection);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Gagal untuk terhubung ke database: " + e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @FXML
    public void aturRegis() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/Register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal membuka halaman registrasi: " + e.getMessage());
        }
    }

    @FXML
    public void aturLog() {
        setUsername(usernameField.getText());
        setPassword(passwordField.getText());

        Integer userId = loginModel.verifyLogin(getUsername(), getPassword());
        if (userId != null) {
            System.out.println("Login berhasil untuk user : " + getUsername());
            notifBerhasilLog();

            String userRole = loginModel.getUserRole(getUsername());
            if (userRole != null) {
                if (userRole.equalsIgnoreCase("admin")) {
                    ggllaman("/fxml/admin/AdminDashboard.fxml");
                } else if (userRole.equalsIgnoreCase("user")) {
                    ggllaman("/fxml/destinasi/Destinasi.fxml");
                } else {
                    System.err.println("Role user tidak valid.");
                }
            } else {
                System.err.println("Role user tidak ditemukan.");
            }
        } else {
            System.out.println("Login gagal untuk user : " + getUsername());
            notifGagalLog();
        }
    }

    private void notifBerhasilLog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil Untuk Login!");
        alert.setHeaderText(null);
        alert.setContentText(getUsername() + " berhasil login.");
        alert.showAndWait();
    }

    private void notifGagalLog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Gagal!");
        alert.setHeaderText(null);
        alert.setContentText("Username atau password salah.");
        alert.showAndWait();
    }

    private void ggllaman(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Gagal memuat laman: " + e.getMessage());
        }
    }

    @FXML
    private void atrliatpasslog() {
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
