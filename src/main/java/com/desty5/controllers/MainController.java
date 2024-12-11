package com.desty5.controllers;

import com.desty5.models.DatabaseModel;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.List;

public class MainController {
    @FXML
    private TextField nameField;

    @FXML
    private ListView<String> nameList;

    private DatabaseModel databaseModel;

    public MainController() {
        try {
            databaseModel = new DatabaseModel(); // Inisialisasi databaseModel
            System.out.println("DatabaseModel initialized successfully!"); // Debug log
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize DatabaseModel: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd() {
        String name = nameField.getText();
        System.out.println("Input received: " + name); // Debug log
        if (name != null && !name.isEmpty()) {
            databaseModel.addName(name); // Tambahkan nama ke backend
            System.out.println("Added to database: " + name); // Debug log
            updateList(); // Perbarui tampilan daftar
            nameField.clear(); // Kosongkan field input
        } else {
            System.out.println("Name field is empty!"); // Debug log
        }
    }

    private void updateList() {
        List<String> names = databaseModel.getNames();
        System.out.println("Names from database: " + names); // Debug log
        nameList.getItems().setAll(names);
    }
}
