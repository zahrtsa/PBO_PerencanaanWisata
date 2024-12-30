package com.desty5.controllers.admin.destinasi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AdminEditDestinasiController {

    @FXML
    private TextField namaField;
    @FXML
    private TextField lokasiField;
    @FXML
    private TextField deskripsiField;
    @FXML
    private TextField hargaField;
    @FXML
    private ImageView gambarImageView;

    private File selectedImageFile;
    private String gambarFilePath;

    private AdminDestinasiModel destinasi;

    public void loadDestinasi(AdminDestinasiModel destinasi) {
        this.destinasi = destinasi;

        namaField.setText(destinasi.getNama());
        lokasiField.setText(destinasi.getLokasi());
        deskripsiField.setText(destinasi.getDeskripsi());
        hargaField.setText(String.valueOf(destinasi.getHargaDestinasi()));
        gambarFilePath = destinasi.getGambar();

        if (gambarFilePath != null && !gambarFilePath.isEmpty()) {
            loadImage(gambarFilePath);
        }
    }

    private void loadImage(String imagePath) {
        try {
            Image image = new Image("file:" + imagePath);
            gambarImageView.setImage(image);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Gambar tidak dapat dimuat.");
        }
    }

    @FXML
    public void uploadGambar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        selectedImageFile = fileChooser.showOpenDialog(null);
        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            gambarImageView.setImage(image);

            String gambarFileName = generateUniqueFileName(namaField.getText());
            gambarFilePath = "img/" + gambarFileName;

            try {
                File imageFolder = new File("src/main/resources/img");
                if (!imageFolder.exists()) {
                    imageFolder.mkdirs();
                }

                File destinationFile = new File(imageFolder, gambarFileName);
                Files.copy(selectedImageFile.toPath(), destinationFile.toPath());
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan gambar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void updateDestinasi() {
        if (destinasi == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Destinasi tidak ditemukan.");
            return;
        }

        String nama = namaField.getText().trim();
        String lokasi = lokasiField.getText().trim();
        String deskripsi = deskripsiField.getText().trim();
        String hargaStr = hargaField.getText().trim();

        if (nama.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty() || hargaStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field harus diisi, termasuk gambar.");
            return;
        }

        try {
            double harga = Double.parseDouble(hargaStr);

            if (!nama.equals(destinasi.getNama()) && selectedImageFile != null) {
                String newGambarFileName = generateUniqueFileName(nama);
                String oldImagePath = gambarFilePath;

                gambarFilePath = "img/" + newGambarFileName;

                try {
                    File oldImageFile = new File("src/main/resources/" + oldImagePath);
                    File imageFolder = new File("src/main/resources/img");
                    if (!imageFolder.exists()) {
                        imageFolder.mkdirs();
                    }

                    File newImageFile = new File(imageFolder, newGambarFileName);
                    if (oldImageFile.exists()) {
                        Files.move(oldImageFile.toPath(), newImageFile.toPath());
                    }
                } catch (IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal mengganti nama file gambar: " + e.getMessage());
                    return;
                }
            }

            try (Connection connection = new DatabaseConnection().getConnection()) {
                AdminDestinasiModel.updateDestinasi(connection, destinasi.getId(), gambarFilePath, nama, lokasi, deskripsi, harga);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Destinasi berhasil diperbarui!");
                navigateToAdminDestinasiController();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memperbarui data: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Harga harus berupa angka desimal.");
        }
    }

    @FXML
    public void deleteDestinasi() {
        if (destinasi == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Destinasi tidak ditemukan.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Apakah Anda yakin ingin menghapus destinasi ini?", ButtonType.YES, ButtonType.NO);
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try (Connection connection = new DatabaseConnection().getConnection()) {
                    System.out.println("ID destinasi yang akan dihapus: " + destinasi.getId());
                    AdminDestinasiModel.hapusDestinasi(connection, destinasi.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Destinasi berhasil dihapus!");
                    navigateToAdminDestinasiController();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus destinasi: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void mundur() {
        navigateToAdminDestinasiController();
    }

    private void navigateToAdminDestinasiController() {
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

    private String generateUniqueFileName(String destinationName) {
        if (selectedImageFile == null) {
            return gambarFilePath;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(new Date());
        String extension = getFileExtension(selectedImageFile.getName());
        return "img-wisata-" + timestamp + "-" + destinationName.replaceAll(" ", "_") + extension;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : fileName.substring(lastDotIndex);
    }
}
