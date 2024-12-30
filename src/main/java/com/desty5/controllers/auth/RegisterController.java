package com.desty5.controllers.auth;

import com.desty5.models.DatabaseConnection;
import com.desty5.models.LoginModel;
import com.desty5.models.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {
    private LoginModel loginModel;

    private String nama;
    private String username;
    private String email;
    private String nomorTelepon;
    private String kataSandi;
    private String konfirmasiKataSandi;

    @FXML
    private TextField namaField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nomorTeleponField;
    @FXML
    private PasswordField kataSandiField;
    @FXML
    private PasswordField konfirmasiKataSandiField;
    @FXML
    private TextField kataSandiLiatField;
    @FXML
    private TextField konfirmasiKataSandiLiatField;
    @FXML
    private CheckBox buatCheckbox;

    public RegisterController() {
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            this.loginModel = new LoginModel(dbConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getKataSandi() {
        return kataSandi;
    }

    public void setKataSandi(String kataSandi) {
        this.kataSandi = kataSandi;
    }

    public String getKonfirmasiKataSandi() {
        return konfirmasiKataSandi;
    }

    public void setKonfirmasiKataSandi(String konfirmasiKataSandi) {
        this.konfirmasiKataSandi = konfirmasiKataSandi;
    }

    @FXML
    public void buatCheckbox() {
        if (buatCheckbox.isSelected()) {
            kataSandiLiatField.setText(kataSandiField.getText());
            konfirmasiKataSandiLiatField.setText(konfirmasiKataSandiField.getText());
    
            kataSandiLiatField.setVisible(true);
            kataSandiField.setVisible(false);
    
            konfirmasiKataSandiLiatField.setVisible(true);
            konfirmasiKataSandiField.setVisible(false);
        } else {
            kataSandiField.setText(kataSandiLiatField.getText());
            konfirmasiKataSandiField.setText(konfirmasiKataSandiLiatField.getText());
    
            kataSandiField.setVisible(true);
            kataSandiLiatField.setVisible(false);
    
            konfirmasiKataSandiField.setVisible(true);
            konfirmasiKataSandiLiatField.setVisible(false);
        }
    }
    
    @FXML
    public void buatSubmit() {
        setNama(namaField.getText());
        setUsername(usernameField.getText());
        setEmail(emailField.getText());
        setNomorTelepon(nomorTeleponField.getText());
        setKataSandi(kataSandiField.getText());
        setKonfirmasiKataSandi(konfirmasiKataSandiField.getText());
    
        if (getNama().isEmpty() || getUsername().isEmpty() || getEmail().isEmpty() ||
            getNomorTelepon().isEmpty() || getKataSandi().isEmpty() || getKonfirmasiKataSandi().isEmpty()) {
            showAlert("Error", "Semua form harus diisi!");
            return;
        }
    
        if (!getEmail().contains("@")) {
            showAlert("Error", "Email harus mengandung '@'!");
            return;
        }
    
        System.out.println("Password input: " + getKataSandi());
        if (!isValidPassword(getKataSandi())) {
            showAlert("Error", "Kata sandi harus kombinasi huruf, angka, dan karakter khusus 8 Karakter!");
            return;
        }
        
    
        if (!getKataSandi().equals(getKonfirmasiKataSandi())) {
            showAlert("Error", "Kata sandi tidak cocok!");
            return;
        }
    
        loginModel.insertUser(getNama(), getUsername(), getEmail(), getNomorTelepon(), getKataSandi(), "user");

        Integer userId = loginModel.verifyLogin(getUsername(), getKataSandi());
        if (userId != null) {
            SessionManager.setCurrentUserId(userId);
            showAlert("Success", "Akun berhasil dibuat dan sesi dimulai!");
        } else {
            showAlert("Error", "Gagal login setelah pendaftaran.");
        }
        
        balikKeLogin();
    }

    @FXML
    public void balikKeLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth/login.fxml"));
            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidPassword(String password) {
        boolean isValid = password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$");
        System.out.println("Validasi kata sandi: " + isValid);
        return isValid;
    }
}
