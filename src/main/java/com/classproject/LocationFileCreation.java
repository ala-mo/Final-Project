package com.classproject;

/**
 * LocationFileCreation
 * --------------------
 * Interface that defines file-related operations for locations.
 * This separates WHAT file actions are required from HOW they are implemented.
 *
 * Demonstrates:
 * - Interfaces
 * - Method declarations
 * - Abstraction
 */
public interface LocationFileCreation {

    /**
     * Creates a file for a new location if it does not already exist.
     */
    void locationFileCreation(String locationName);

    /**
     * Appends a rating and review to an existing location file.
     */
    void addingRatingToFile(String locationName, int rating, String review);
}
