package com.classproject;

/**
 * Location
 * --------
 * initializing variables for location
     - String name (location name)
     - String description (description for location)
     - int ratingCount (number for rating)
 * initializing arrays for location
     - int[] ratings (to hold all of the inputted ratings)
     - String[] reviews (to hold all of the inputted reviews)
 * methods 
     - Location(String name, String description) ; to store location name and description
     - addRating(int rating, String review) ; storing ratings and reviews into array
     - double getRating() ; returns the average of the ratings for each location
     - String getName() ; returns private String name
     - String getDescription() ; return private String description
     - int[] getRatings() ; takes user's rating and displays using JavaFX
     - String[] getReviews() ; takes user's review and displays using JavaFX
 */
public class Location {

    private String name;
    private String description;

    private int[] ratings = new int[100];
    private String[] reviews = new String[100];
    private int ratingCount = 0;

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

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
