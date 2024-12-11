package com.desty5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Pastikan jalur FXML sesuai dengan struktur proyek Anda
            URL fxmlLocation = getClass().getResource("/fxml/Main.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML file not found at /fxml/Main.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(loader.load());
            
            // Set judul dan tampilan
            primaryStage.setTitle("Desty5 Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            // Tampilkan detail kesalahan untuk debugging
            e.printStackTrace();
            System.err.println("Failed to start application due to: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
