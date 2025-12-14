package com.classproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

// Correct Inheritance and Interface Implementation
public class MainApplication extends LocationPage implements LocationFileCreation {


    @Override
    public void start(Stage stage) throws Exception {
        // Simple UI element to show that it works
        Label label = new Label("JavaFX setup complete!");
        StackPane root = new StackPane(label);

        Scene scene = new Scene(root, 320, 240);

        stage.setTitle("Final Project App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // This launch() calls the one inherited from javafx.application.Application
        launch(args);
    }

    // --- Implementation of Abstract/Interface Methods (Leave empty for now) ---

    @Override
    public void locationFileCreation(String locationName) {
        // TODO: File I/O logic goes here
    }

    @Override
    public void addingRatingToFile(int rating) {
        // TODO: File I/O logic goes here
    }

    @Override
    public void displayLocationDetails() {
        // TODO: JavaFX display logic goes here
    }
}