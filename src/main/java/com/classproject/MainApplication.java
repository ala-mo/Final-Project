package com.classproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApplication extends Application {

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
        // The main method launches the JavaFX application
        launch(args);
    }
}