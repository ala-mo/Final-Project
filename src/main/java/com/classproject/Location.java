package com.classproject;

/**
 * Location
 * --------
 * Data model representing a travel location.
 *
 * Responsibilities:
 * - Store name and description
 * - Store ratings and reviews using arrays
 * - Calculate average rating
 *
 * This class contains NO GUI code and NO file-handling code.
 * Demonstrates encapsulation and object-oriented design.
 */
public class Location {

    private String name;
    private String description;

    // Arrays used instead of ArrayLists (per course requirements)
    private int[] ratings = new int[100];
    private String[] reviews = new String[100];
    private int ratingCount = 0;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /* =========================
       RATING LOGIC
       ========================= */

    public void addRating(int rating, String review) {
        if (ratingCount < ratings.length) {
            ratings[ratingCount] = rating;
            reviews[ratingCount] = review;
            ratingCount++;
        }
    }

    public double getRating() {
        if (ratingCount == 0) return 0.0;
        int sum = 0;
        for (int i = 0; i < ratingCount; i++) {
            sum += ratings[i];
        }
        return (double) sum / ratingCount;
    }

    /* =========================
       GETTERS
       ========================= */

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int[] getRatings() {
        int[] copy = new int[ratingCount];
        System.arraycopy(ratings, 0, copy, 0, ratingCount);
        return copy;
    }

    public String[] getReviews() {
        String[] copy = new String[ratingCount];
        System.arraycopy(reviews, 0, copy, 0, ratingCount);
        return copy;
    }
}
