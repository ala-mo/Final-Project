package com.classproject;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * LocationDetailView
 * ------------------
 * JavaFX features for Location Page
     - TextField ratingField ; requests input for ratings
     - TextArea reviewField ; requests input for reviews 
     - Button submitBtn ; to submit input for review
     - Button deleteBtn ; to delete location
     - Button backBtn ; to exit page
 */
public class LocationDetailView extends LocationPage {

    private Location location;

    public LocationDetailView(Stage stage, Location location) {
        this.stage = stage;
        this.location = location;
        this.locationName = location.getName();
    }

    @Override
    public void displayLocationDetails() {

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));

        Label nameLabel = new Label("Location: " + location.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label descLabel = new Label("Description: " + location.getDescription());

        Label ratingLabel = new Label(
                "Average Rating: " + String.format("%.1f", location.getRating())
        );
        
        TextField ratingField = new TextField();
        ratingField.setPromptText("Enter rating (1–5)");

        TextArea reviewField = new TextArea();
        reviewField.setPromptText("Write your review here...");
        reviewField.setPrefRowCount(4);

        Button submitBtn = new Button("Submit Review");
        Button deleteBtn = new Button("Delete Location");
        Button backBtn = new Button("Back");

        Label messageLabel = new Label();

        submitBtn.setOnAction(e -> {
            String ratingText = ratingField.getText().trim();
            String reviewText = reviewField.getText().trim();

            if (ratingText.isEmpty() || reviewText.isEmpty()) {
                messageLabel.setText("Please enter both a rating and a review.");
                return;
            }

            int rating;
            try {
                rating = Integer.parseInt(ratingText);
                if (rating < 1 || rating > 5) {
                    messageLabel.setText("Rating must be between 1 and 5.");
                    return;
                }
            } catch (NumberFormatException ex) {
                messageLabel.setText("Rating must be a number.");
                return;
            }

            // Update location data
            location.addRating(rating, reviewText);

            // Save changes to file
            LocationFileManager.saveLocationToFile(location);

            // Update display
            ratingLabel.setText(
                    "Average Rating: " + String.format("%.1f", location.getRating())
            );

            messageLabel.setText("Review added successfully!");

            ratingField.clear();
            reviewField.clear();
        });

        deleteBtn.setOnAction(e -> {
            Alert confirm = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this location?",
                    ButtonType.YES,
                    ButtonType.NO
            );

            confirm.showAndWait();

            if (confirm.getResult() == ButtonType.YES) {
                LocationFileManager.deleteLocationFile(location.getName());
                stage.setScene(MainApplication.buildHomeSceneStatic(stage));
            }
        });

        backBtn.setOnAction(e ->
                stage.setScene(MainApplication.buildHomeSceneStatic(stage))
        );

        root.getChildren().addAll(
                nameLabel,
                descLabel,
                ratingLabel,
                ratingField,
                reviewField,
                submitBtn,
                deleteBtn,
                backBtn,
                messageLabel
        );

        stage.setScene(new Scene(root, 450, 520));
    }
}
