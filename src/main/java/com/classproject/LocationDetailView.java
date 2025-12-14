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
 * Displays all previously saved journal entries (ratings + reflections).
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

        /* ---------- DISPLAY LOCATION INFO ---------- */

        Label nameLabel = new Label("Location: " + location.getName());
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label descLabel = new Label("Description: " + location.getDescription());

        /* ---------- DISPLAY JOURNAL ENTRIES ---------- */

        Label entriesTitle = new Label("Journal Entries:");
        entriesTitle.setStyle("-fx-font-weight: bold;");

        VBox entriesBox = new VBox(5);

        int[] ratings = location.getRatings();
        String[] reviews = location.getReviews();

        if (ratings.length == 0) {
            entriesBox.getChildren().add(new Label("No journal entries yet."));
        } else {
            for (int i = 0; i < ratings.length; i++) {
                Label entryLabel = new Label(
                        "⭐ " + ratings[i] + " — " + reviews[i]
                );
                entriesBox.getChildren().add(entryLabel);
            }
        }

        /* ---------- INPUT FIELDS ---------- */

        TextField ratingField = new TextField();
        ratingField.setPromptText("Rate your experience (1–5)");

        TextArea reviewField = new TextArea();
        reviewField.setPromptText("Write your journal entry here...");
        reviewField.setPrefRowCount(4);

        /* ---------- BUTTONS ---------- */

        Button submitBtn = new Button("Add Journal Entry");
        Button deleteBtn = new Button("Delete Location");
        Button backBtn = new Button("Back");

        Label messageLabel = new Label();

        /* ---------- SUBMIT LOGIC ---------- */

        submitBtn.setOnAction(e -> {
            String ratingText = ratingField.getText().trim();
            String reviewText = reviewField.getText().trim();

            if (ratingText.isEmpty() || reviewText.isEmpty()) {
                messageLabel.setText("Please enter both a rating and a journal entry.");
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

            // Add new journal entry
            location.addRating(rating, reviewText);

            // Save changes to file
            LocationFileManager.saveLocationToFile(location);

            // Update UI immediately
            entriesBox.getChildren().add(
                    new Label("⭐ " + rating + " — " + reviewText)
            );

            messageLabel.setText("Journal entry added successfully!");

            ratingField.clear();
            reviewField.clear();
        });

        /* ---------- DELETE LOGIC ---------- */

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

        /* ---------- NAVIGATION ---------- */

        backBtn.setOnAction(e ->
                stage.setScene(MainApplication.buildHomeSceneStatic(stage))
        );

        /* ---------- LAYOUT ---------- */

        root.getChildren().addAll(
                nameLabel,
                descLabel,
                entriesTitle,
                entriesBox,
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
