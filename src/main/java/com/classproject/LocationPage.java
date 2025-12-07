package com.classproject;

import javafx.application.Application;

public abstract class LocationPage extends Application {

// Variables required by your plan

    protected String locationName; // Example of a variable

    protected int locationRatingAddition; // Example of a variable



// A concrete method that all children can use (Standardize formatting for each location)

    public void standardizeDisplayFormat() {

        System.out.println("--- Displaying standardized format for: " + this.locationName + " ---");

    }



// An abstract method that FORCES child classes (like MainApplication) to implement specific display behavior

    public abstract void displayLocationDetails();

}
