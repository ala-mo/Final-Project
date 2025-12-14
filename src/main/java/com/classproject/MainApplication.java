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
 * JavaFX features
     - Label clockLabel ; label for the clock
     - Scene buildHomeScene(Stage stage) ; setting the stage for the home page
     - Scene buildAddLocationScene(Stage stage) ; setting the stage for the location page
 * methods
     - startClockThread() ; multithreading the clock
     - populateSearchResults(VBox box, String query, Stage stage) ; search results
     - Scene buildHomeSceneStatic(Stage stage) ; updates changes to the homepage upon return
 */
public class MainApplication extends Application {

    private Map<String, Location> locations = new HashMap<>();
    
    private Label clockLabel = new Label();

    @Override
    public void start(Stage stage) {
        locations = LocationFileManager.loadLocationsFromFiles();
        stage.setTitle("My Travel Journal");
        stage.setScene(buildHomeScene(stage));
        stage.show();

        startClockThread();
    }

    private void startClockThread() {
        Thread clockThread = new Thread(() -> {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("hh:mm:ss a");

            while (true) {
                String currentTime = LocalTime.now().format(formatter);

                Platform.runLater(() ->
                        clockLabel.setText("Current Time: " + currentTime)
                );

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        clockThread.setDaemon(true);
        clockThread.start();
    }

    public Scene buildHomeScene(Stage stage) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("My Travel Journal");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

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
                subtitle,
                clockLabel,
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
                Button btn = new Button(loc.getName());
                btn.setOnAction(e ->
                        new LocationDetailView(stage, loc).displayLocationDetails()
                );
                box.getChildren().add(btn);
            }
        }
    }

    private Scene buildAddLocationScene(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        TextField nameField = new TextField();
        TextField ratingField = new TextField();
        TextArea entryField = new TextArea();

        nameField.setPromptText("Location Name");
        ratingField.setPromptText("Initial rating (1–5)");
        entryField.setPromptText("Add your first journal entry...");
        entryField.setPrefRowCount(4);

        Label message = new Label();
        Button save = new Button("Save");
        Button back = new Button("Back");

        save.setOnAction(e -> {
            String name = nameField.getText().trim();
            String entryText = entryField.getText().trim();
            String ratingText = ratingField.getText().trim();

            if (name.isEmpty() || entryText.isEmpty() || ratingText.isEmpty()) {
                message.setText("Please fill in all fields.");
                return;
            }

            int rating;
            try {
                rating = Integer.parseInt(ratingText);
                if (rating < 1 || rating > 5) {
                    message.setText("Rating must be between 1 and 5.");
                    return;
                }
            } catch (NumberFormatException ex) {
                message.setText("Rating must be a number.");
                return;
            }

            Location loc = new Location(name, "");
            loc.addRating(rating, entryText);

            locations.put(loc.getName(), loc);
            LocationFileManager.saveLocationToFile(loc);

            message.setText("Location added!");
            nameField.clear();
            ratingField.clear();
            entryField.clear();
        });

        back.setOnAction(e -> stage.setScene(buildHomeScene(stage)));

        root.getChildren().addAll(
                nameField,
                ratingField,
                entryField,
                save,
                back,
                message
        );

        return new Scene(root, 450, 540);
    }

    public static Scene buildHomeSceneStatic(Stage stage) {
        MainApplication app = new MainApplication();
        app.locations = LocationFileManager.loadLocationsFromFiles();
        return app.buildHomeScene(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
