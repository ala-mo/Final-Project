package com.classproject;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * LocationPage
 * ------------
 * Abstract class representing a page related to a single location.
 *
 * Enforces that all location pages must define how they are displayed.
 * Provides shared variables and behavior.
 */
public abstract class LocationPage extends Application {

    protected String locationName;
    protected int locationRatingAddition;
    protected Stage stage;

    public void standardizeDisplayFormat() {
        System.out.println("--- Displaying standardized format for: " + locationName + " ---");
    }

    public abstract void displayLocationDetails();

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        displayLocationDetails();
    }
}
