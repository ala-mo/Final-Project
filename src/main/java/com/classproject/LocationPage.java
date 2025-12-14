package com.classproject;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * LocationPage
 * ------------
 * methods
     - standardizeDisplayFormat() ; emphasizes a standard display for all location pages
     - displayLocationDetails() ; initialized
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
