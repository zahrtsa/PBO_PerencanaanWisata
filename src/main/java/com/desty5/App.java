package com.desty5;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlLocation = getClass().getResource("/fxml/auth/login.fxml");
            if (fxmlLocation == null) {
                throw new RuntimeException("FXML file not found at /fxml/auth/login.fxml");
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(loader.load());
            
            primaryStage.setTitle("Desty5 Application");
            primaryStage.setScene(scene);

            URL iconLocation = getClass().getResource("/img/logo/logo.png");
            if (iconLocation != null) {
                primaryStage.getIcons().add(new Image(iconLocation.toExternalForm()));
            } else {
                System.err.println("Logo not found at /img/logo/logo.png");
            }

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start application due to: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
