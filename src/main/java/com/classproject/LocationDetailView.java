package com.classproject;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * LocationDetailView
 * ------------------
 * Displays details for a single location.
 * Allows users to add a rating (1–5) and a text review.
 *
 * This class represents the "individual location page"
 * described in the project rubric.
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

        /* ---------- DISPLAY CURRENT INFO ---------- */

        Label nameLabel = new Label("Location: " + location.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label descLabel = new Label("Description: " + location.getDescription());

        Label ratingLabel = new Label(
                "Average Rating: " + String.format("%.1f", location.getRating())
        );

        /* ---------- INPUT FIELDS ---------- */

        TextField ratingField = new TextField();
        ratingField.setPromptText("Enter rating (1–5)");

        TextArea reviewField = new TextArea();
        reviewField.setPromptText("Write your review here...");
        reviewField.setPrefRowCount(4);

        /* ---------- BUTTONS ---------- */

        Button submitBtn = new Button("Submit Review");
        Button backBtn = new Button("Back");

        Label messageLabel = new Label();

        /* ---------- SUBMIT LOGIC ---------- */

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

        /* ---------- NAVIGATION ---------- */

        backBtn.setOnAction(e ->
                stage.setScene(MainApplication.buildHomeSceneStatic(stage))
        );

        /* ---------- LAYOUT ---------- */

        root.getChildren().addAll(
                nameLabel,
                descLabel,
                ratingLabel,
                ratingField,
                reviewField,
                submitBtn,
                backBtn,
                messageLabel
        );

        stage.setScene(new Scene(root, 450, 520));
    }
}
