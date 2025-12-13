package com.classproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * MainApplication
 * ----------------
 * Entry point of the program.
 *
 * PROGRAM FLOW:
 * 1. Load saved locations from files
 * 2. Display home screen with search
 * 3. Allow users to add locations
 * 4. Allow users to view and review locations
 * 5. Display a real-time clock using multithreading
 *
 * Handles scene switching and user interaction.
 */
public class MainApplication extends Application {

    private Map<String, Location> locations = new HashMap<>();

    // Clock label updated by a background thread
    private Label clockLabel = new Label();

    @Override
    public void start(Stage stage) {
        locations = LocationFileManager.loadLocationsFromFiles();
        stage.setTitle("My Travel Journal");
        stage.setScene(buildHomeScene(stage));
        stage.show();

        // Start the multithreaded clock
        startClockThread();
    }

    /* =========================
       MULTITHREADED CLOCK
       ========================= */

    private void startClockThread() {

        Thread clockThread = new Thread(() -> {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("hh:mm:ss a");

            while (true) {
                String currentTime = LocalTime.now().format(formatter);

                // Update JavaFX UI safely
                Platform.runLater(() ->
                        clockLabel.setText("Current Time: " + currentTime)
                );

                try {
                    Thread.sleep(1000); // update every second
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        clockThread.setDaemon(true); // stops when app closes
        clockThread.start();
    }

    /* =========================
       HOME SCREEN
       ========================= */

    public Scene buildHomeScene(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("My Travel Journal");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        // ✅ SUBTITLE ADDED HERE
        Label subtitle = new Label("Save places you’ve been and reflect on your experiences");
        subtitle.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");

        clockLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        TextField searchField = new TextField();
        searchField.setPromptText("Search for a location");

        Button searchBtn = new Button("Search");
        Button addBtn = new Button("Add Location");

        VBox resultsBox = new VBox(10);
        populateSearchResults(resultsBox, null, stage);

        searchBtn.setOnAction(e ->
                populateSearchResults(
                        resultsBox,
                        searchField.getText().toLowerCase(),
                        stage
                )
        );

        addBtn.setOnAction(e -> stage.setScene(buildAddLocationScene(stage)));

        root.getChildren().addAll(
                title,
                subtitle,     // 👈 subtitle placed under title
                clockLabel,   // 👈 clock below subtitle
                searchField,
                searchBtn,
                addBtn,
                resultsBox
        );

        return new Scene(root, 450, 560);
    }

    private void populateSearchResults(VBox box, String query, Stage stage) {
        box.getChildren().clear();

        for (Location loc : locations.values()) {
            if (query == null || loc.getName().toLowerCase().contains(query)) {
                Button btn = new Button(loc.getName() + " (⭐ " + loc.getRating() + ")");
                btn.setOnAction(e ->
                        new LocationDetailView(stage, loc).displayLocationDetails());
                box.getChildren().add(btn);
            }
        }
    }

    /* =========================
       ADD LOCATION SCREEN
       ========================= */

    private Scene buildAddLocationScene(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        TextField name = new TextField();
        TextArea desc = new TextArea();
        TextField rating = new TextField();

        name.setPromptText("Location Name");
        desc.setPromptText("Description");
        rating.setPromptText("Initial Rating (1–5)");

        Label message = new Label();
        Button save = new Button("Save");
        Button back = new Button("Back");

        save.setOnAction(e -> {
            try {
                Location loc = new Location(name.getText(), desc.getText());
                loc.addRating(Integer.parseInt(rating.getText()), desc.getText());
                locations.put(loc.getName(), loc);
                LocationFileManager.saveLocationToFile(loc);
                message.setText("Location added!");
            } catch (Exception ex) {
                message.setText("Invalid input.");
            }
        });

        back.setOnAction(e -> stage.setScene(buildHomeScene(stage)));

        root.getChildren().addAll(name, desc, rating, save, back, message);
        return new Scene(root, 450, 520);
    }

    /* =========================
       STATIC HOME BUILDER
       ========================= */

    public static Scene buildHomeSceneStatic(Stage stage) {
        MainApplication app = new MainApplication();
        app.locations = LocationFileManager.loadLocationsFromFiles();
        return app.buildHomeScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
