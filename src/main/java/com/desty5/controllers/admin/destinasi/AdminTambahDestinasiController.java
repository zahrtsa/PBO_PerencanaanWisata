package com.desty5.controllers.admin.destinasi;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.desty5.models.AdminDestinasiModel;
import com.desty5.models.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminTambahDestinasiController {

    @FXML
    private TextField namaField;
    @FXML
    private TextField lokasiField;
    @FXML
    private TextField deskripsiField;
    @FXML
    private TextField hargaField;
    @FXML
    private ImageView imageView;

    private File selectedImageFile;
    private String gambarFilePath;

    @FXML
public void submitDestinasi() {
    String nama = namaField.getText().trim();
    String lokasi = lokasiField.getText().trim();
    String deskripsi = deskripsiField.getText().trim();
    String hargaStr = hargaField.getText().trim();

    if (nama.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty() || hargaStr.isEmpty() || selectedImageFile == null) {
        showAlert(Alert.AlertType.ERROR, "Error", "Semua field harus diisi, termasuk gambar.");
        return;
    }

    try {
        double harga = Double.parseDouble(hargaStr);

        String gambarFileName = generateUniqueFileName(nama);
        gambarFilePath = "img/" + gambarFileName;

        File imageFolder = new File("src/main/resources/img");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }

        File destinationFile = new File(imageFolder, gambarFileName);
        java.nio.file.Files.copy(selectedImageFile.toPath(), destinationFile.toPath());

        try (Connection connection = new DatabaseConnection().getConnection()) {
            AdminDestinasiModel.tambahDestinasi(connection, gambarFilePath, nama, lokasi, deskripsi, harga);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Destinasi berhasil ditambahkan!");
            navigateToAdminDestinasiController();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menambahkan data: " + e.getMessage());
        }
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Error", "Harga harus berupa angka desimal.");
    } catch (IOException e) {
        showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan gambar: " + e.getMessage());
    }
}


    @FXML
    public void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        
        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
        }
    }
    
    private String generateUniqueFileName(String destinationName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String extension = getFileExtension(selectedImageFile.getName());
        return "img-wisata-" + timestamp + "-" + destinationName.replaceAll(" ", "_") + extension;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex);
    }

    @FXML
    public void navigateToAdminDestinasiController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/admin/destinasi/AdminViewDestinasi.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) namaField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat halaman destinasi.");
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
