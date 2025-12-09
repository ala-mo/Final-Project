package com.classproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApplication extends LocationPage implements LocationFileCreation {

    private String[] locationNames = new String[10];
    private int[] locationRatings = new int[10]; // To store average rating for simplicity

    public MainApplication() {
        // Initialize some locations
        locationNames[0] = "Eiffel Tower";
        locationNames[1] = "Colosseum";
        locationNames[2] = "Statue of Liberty";
        locationRatings[0] = 5;
        locationRatings[1] = 4;
        locationRatings[2] = 3;
    }

    @Override
    public void start(Stage stage) {
        // Home GUI Layout
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: top_center;");

        Label titleLabel = new Label("Travel Guide App");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter location name to search");

        Button searchButton = new Button("Search");

        VBox resultsBox = new VBox(5); // For search results or full list
        resultsBox.setStyle("-fx-padding: 10;");

        // Display all locations initially
        displayAllLocations(resultsBox);

        // Handle Search Button
        searchButton.setOnAction(e -> {
            String query = searchBar.getText().trim().toLowerCase();
            resultsBox.getChildren().clear();

            boolean found = false;
            for (int i = 0; i < locationNames.length; i++) {
                if (locationNames[i] != null && locationNames[i].toLowerCase().contains(query)) {
                    Button locationButton = createLocationButton(i);
                    resultsBox.getChildren().add(locationButton);
                    found = true;
                }
            }

            if (!found) {
                resultsBox.getChildren().add(new Label("Can’t find what you’re looking for"));
            }
        });

        root.getChildren().addAll(titleLabel, searchBar, searchButton, resultsBox);

        Scene scene = new Scene(root, 500, 400);
        stage.setTitle("Travel Guide App");
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to display all locations
    private void displayAllLocations(VBox box) {
        for (int i = 0; i < locationNames.length; i++) {
            if (locationNames[i] != null && !locationNames[i].isEmpty()) {
                Button locationButton = createLocationButton(i);
                box.getChildren().add(locationButton);
            }
        }
    }

    // Create a button for a location
    private Button createLocationButton(int index) {
        Button btn = new Button(locationNames[index] + " (Rating: " + locationRatings[index] + "/5)");
        btn.setOnAction(e -> {
            this.locationName = locationNames[index];
            this.locationRatingAddition = locationRatings[index];
            displayLocationDetails(); // Opens detail window
        });
        return btn;
    }

    @Override
    public void locationFileCreation(String locationName) {
        try (java.io.FileWriter writer = new java.io.FileWriter("locations/" + locationName + ".txt")) {
            writer.write("Location: " + locationName + "\n");
            writer.write("Average Rating: 0\n");
            writer.write("--- REVIEWS ---\n");
        } catch (java.io.IOException e) {
            System.err.println("ERROR: Could not create file for " + locationName);
        }
    }

    @Override
    public void addingRatingToFile(int rating) {
        // For now, just update array rating (files can be added later)
        this.locationRatingAddition = rating;
    }

    @Override
    public void displayLocationDetails() {
        Stage detailStage = new Stage();

        Label nameLabel = new Label("Location: " + this.locationName);
        Label ratingLabel = new Label("Current Rating: " + this.locationRatingAddition + "/5");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(nameLabel, ratingLabel);

        Scene scene = new Scene(layout, 300, 200);
        detailStage.setTitle("Location Details");
        detailStage.setScene(scene);
        detailStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
