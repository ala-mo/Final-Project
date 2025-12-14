package com.classproject;

/**
 * LocationFileCreation
 * --------------------
 * initializing methods 
     - locationFileCreation(String locationName) ; creates a file for new locations
     - addingRatingToFile(String locationName, int rating, String review) ; adds ratings and reviews to location files
 */
public interface LocationFileCreation {

    void locationFileCreation(String locationName);
    
    void addingRatingToFile(String locationName, int rating, String review);
}
