package com.classproject;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;

public class MainApplication extends LocationPage implements LocationFileCreation {

    private String[] locationNames = new String[10];
    private int[] locationRatings = new int[10];

    public MainApplication() {
        loadLocations(); // load saved or default locations
    }

    @Override
    public void start(Stage stage) {

        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: top_center;");

        Label titleLabel = new Label("Travel Guide App");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Search bar
        TextField searchBar = new TextField();
        searchBar.setPromptText("Enter location name to search");
        Button searchButton = new Button("Search");

        // Location display box
        VBox resultsBox = new VBox(5);
        resultsBox.setStyle("-fx-padding: 10;");
        displayAllLocations(resultsBox);

        searchButton.setOnAction(e -> {
            String query = searchBar.getText().trim().toLowerCase();
            resultsBox.getChildren().clear();

            boolean found = false;

            for (int i = 0; i < locationNames.length; i++) {
                if (locationNames[i] != null &&
                        locationNames[i].toLowerCase().contains(query)) {

                    resultsBox.getChildren().add(createLocationButton(i));
                    found = true;
                }
            }

            if (!found) {
                resultsBox.getChildren().add(new Label("Cannot find that location."));
            }
        });

        // Add new location
        HBox addLocationBox = new HBox(10);
        TextField newLocationField = new TextField();
        newLocationField.setPromptText("Enter new location");
        Button addLocationButton = new Button("Add");

        addLocationBox.getChildren().addAll(newLocationField, addLocationButton);

        addLocationButton.setOnAction(e -> {
            String newLocation = newLocationField.getText().trim();

            if (newLocation.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Enter a location name!").showAndWait();
                return;
            }

            boolean added = false;

            for (int i = 0; i < locationNames.length; i++) {
                if (locationNames[i] == null) {
                    locationNames[i] = newLocation;
                    locationRatings[i] = 0;

                    resultsBox.getChildren().add(createLocationButton(i));
                    added = true;

                    locationFileCreation(newLocation);
                    saveLocations();

                    newLocationField.clear();
                    new Alert(Alert.AlertType.INFORMATION, "Location added!").showAndWait();
                    break;
                }
            }

            if (!added) {
                new Alert(Alert.AlertType.ERROR, "No space left for more locations!").showAndWait();
            }
        });

        root.getChildren().addAll(
                titleLabel, searchBar, searchButton,
                resultsBox,
                new Label("Add a New Location:"),
                addLocationBox
        );

        stage.setTitle("Travel Guide App");
        stage.setScene(new Scene(root, 500, 500));
        stage.show();
    }

    // Display all saved locations
    private void displayAllLocations(VBox box) {
        box.getChildren().clear();
        for (int i = 0; i < locationNames.length; i++) {
            if (locationNames[i] != null) {
                box.getChildren().add(createLocationButton(i));
            }
        }
    }

    // Button for each location
    private Button createLocationButton(int index) {
        Button btn = new Button(locationNames[index] + " (Rating: " + locationRatings[index] + "/5)");

        btn.setOnAction(e -> displayLocationDetails(btn, index));

        return btn;
    }

    // Write a file for each location (simple version)
    @Override
    public void locationFileCreation(String locationName) {
        File folder = new File("locations");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (FileWriter writer = new FileWriter("locations/" + locationName + ".txt")) {
            writer.write("Location: " + locationName + "\n");
            writer.write("Rating: 0\n");
        } catch (IOException e) {
            System.err.println("Error creating file for: " + locationName);
        }
    }

    @Override
    public void addingRatingToFile(int rating) {
        // Not used in current version — ratings saved in saveLocations()
    }

    // Show individual location window
    private void displayLocationDetails(Button homeButton, int index) {

        Stage detailStage = new Stage();

        Label nameLabel = new Label("Location: " + locationNames[index]);
        Label ratingLabel = new Label("Current Rating: " + locationRatings[index] + "/5");

        Label addRatingLabel = new Label("Add rating (1–5):");
        TextField ratingField = new TextField();
        Button submitRating = new Button("Submit");

        submitRating.setOnAction(e -> {
            try {
                int newRating = Integer.parseInt(ratingField.getText());

                if (newRating < 1 || newRating > 5) {
                    new Alert(Alert.AlertType.WARNING, "Rating must be 1–5").showAndWait();
                    return;
                }

                locationRatings[index] = newRating;
                ratingLabel.setText("Current Rating: " + newRating + "/5");

                homeButton.setText(locationNames[index] + " (Rating: " + newRating + "/5)");

                saveLocations();

                new Alert(Alert.AlertType.INFORMATION, "Rating updated!").showAndWait();

            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Enter a number 1–5!").showAndWait();
            }
        });

        VBox layout = new VBox(10, nameLabel, ratingLabel, addRatingLabel, ratingField, submitRating);
        layout.setStyle("-fx-padding: 20;");

        detailStage.setScene(new Scene(layout, 300, 250));
        detailStage.setTitle("Location Details");
        detailStage.show();
    }

    // Save all locations to file
    private void saveLocations() {
        File folder = new File("locations");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (FileWriter writer = new FileWriter("locations/locations.txt")) {
            for (int i = 0; i < locationNames.length; i++) {
                if (locationNames[i] != null) {
                    writer.write(locationNames[i] + "|" + locationRatings[i] + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving locations.");
        }
    }

    // Load locations, or defaults if file missing
    private void loadLocations() {
        File file = new File("locations/locations.txt");

        if (!file.exists()) {
            // Default starter locations
            locationNames[0] = "Eiffel Tower";
            locationRatings[0] = 5;

            locationNames[1] = "Colosseum";
            locationRatings[1] = 4;

            locationNames[2] = "Statue of Liberty";
            locationRatings[2] = 3;
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;

            while ((line = reader.readLine()) != null && index < locationNames.length) {
                String[] parts = line.split("\\|");
                if (parts.length == 2) {
                    locationNames[index] = parts[0];
                    locationRatings[index] = Integer.parseInt(parts[1]);
                }
                index++;
            }

        } catch (IOException e) {
            System.err.println("Error loading saved locations.");
        }
    }

    /**
     * Required override for the abstract method declared in LocationPage.
     * This is a simple fallback handler; the actual detailed UI uses
     * displayLocationDetails(Button, int).
     */
    @Override
    public void displayLocationDetails() {
        // Fallback: tell the user to pick a location from the home screen
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select a location from the home screen to view details.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
