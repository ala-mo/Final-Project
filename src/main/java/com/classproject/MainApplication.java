package com.classproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApplication extends LocationPage implements LocationFileCreation {

    private String[] locationNames = new String[10];
    private int[] locationRatings = new int[10]; // Store ratings for simplicity

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
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: top_center;");

        Label titleLabel = new Label("Travel Guide App");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // --- Search Section ---
        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter location name to search");

        Button searchButton = new Button("Search");

        VBox resultsBox = new VBox(5);
        resultsBox.setStyle("-fx-padding: 10;");

        displayAllLocations(resultsBox); // Show all locations initially

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

        // --- Add Location Section ---
        HBox addLocationBox = new HBox(10);
        TextField newLocationField = new TextField();
        newLocationField.setPromptText("Enter new location");
        Button addLocationButton = new Button("Add Location");

        addLocationBox.getChildren().addAll(newLocationField, addLocationButton);

        addLocationButton.setOnAction(e -> {
            String newLocation = newLocationField.getText().trim();
            if (newLocation.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a location name!");
                alert.showAndWait();
                return;
            }

            boolean added = false;
            for (int i = 0; i < locationNames.length; i++) {
                if (locationNames[i] == null || locationNames[i].isEmpty()) {
                    locationNames[i] = newLocation;
                    locationRatings[i] = 0; // default rating
                    Button newBtn = createLocationButton(i);
                    resultsBox.getChildren().add(newBtn);

                    locationFileCreation(newLocation); // create file

                    newLocationField.clear();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Location added!");
                    alert.showAndWait();
                    added = true;
                    break;
                }
            }

            if (!added) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No space to add new location!");
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(titleLabel, searchBar, searchButton, resultsBox, new Label("Add a New Location:"), addLocationBox);

        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Travel Guide App");
        stage.setScene(scene);
        stage.show();
    }

    // --- Helper Methods ---

    // Display all locations on home screen
    private void displayAllLocations(VBox box) {
        for (int i = 0; i < locationNames.length; i++) {
            if (locationNames[i] != null && !locationNames[i].isEmpty()) {
                Button locationButton = createLocationButton(i);
                box.getChildren().add(locationButton);
            }
        }
    }

    // Create a button for a location; clicking opens detail window
    private Button createLocationButton(int index) {
        Button btn = new Button(locationNames[index] + " (Rating: " + locationRatings[index] + "/5)");
        btn.setOnAction(e -> {
            this.locationName = locationNames[index];
            this.locationRatingAddition = locationRatings[index];
            displayLocationDetails(btn, index); // Pass the button to update text after rating
        });
        return btn;
    }

    // Required override to satisfy abstract class
    @Override
    public void displayLocationDetails() {
        // This version is required but won't be called
    }

    // Display location details and allow adding a rating
    private void displayLocationDetails(Button homeButton, int index) {
        Stage detailStage = new Stage();

        Label nameLabel = new Label("Location: " + this.locationName);
        Label ratingLabel = new Label("Current Rating: " + this.locationRatingAddition + "/5");

        Label addRatingLabel = new Label("Add a new rating (1-5):");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Enter rating");

        Button submitRatingButton = new Button("Submit Rating");

        submitRatingButton.setOnAction(e -> {
            String input = ratingField.getText().trim();
            try {
                int newRating = Integer.parseInt(input);
                if (newRating < 1 || newRating > 5) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Rating must be between 1 and 5!");
                    alert.showAndWait();
                    return;
                }

                // Update rating array
                locationRatings[index] = newRating;
                this.locationRatingAddition = newRating;

                // Update label
                ratingLabel.setText("Current Rating: " + this.locationRatingAddition + "/5");

                // Update button text on home screen
                homeButton.setText(locationNames[index] + " (Rating: " + locationRatings[index] + "/5)");

                addingRatingToFile(newRating);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rating added!");
                alert.showAndWait();
                ratingField.clear();

            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid number!");
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20;");
        layout.getChildren().addAll(nameLabel, ratingLabel, addRatingLabel, ratingField, submitRatingButton);

        Scene scene = new Scene(layout, 350, 250);
        detailStage.setTitle("Location Details");
        detailStage.setScene(scene);
        detailStage.show();
    }

    // --- Interface methods ---
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
        // For now, just update array rating (file write can be added later)
        this.locationRatingAddition = rating;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
