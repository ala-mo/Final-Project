package com.classproject;

import java.io.*;
import java.util.*;

/**
 * LocationFileManager
 * -------------------
 * Handles all file input/output for locations.
 * Implements LocationFileCreation interface.
 *
 * Responsibilities:
 * - Create files for locations
 * - Save location data
 * - Load location data on startup
 *
 * Keeps file logic separate from GUI logic.
 */
public class LocationFileManager implements LocationFileCreation {

    private static final String DATA_DIR = "locations";

    public LocationFileManager() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /* =========================
       INTERFACE METHODS
       ========================= */

    @Override
    public void locationFileCreation(String locationName) {
        try {
            File file = new File(DATA_DIR + "/" + locationName + ".txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file for " + locationName);
        }
    }

    @Override
    public void addingRatingToFile(String locationName, int rating, String review) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(DATA_DIR + "/" + locationName + ".txt", true))) {

            writer.write(rating + "|" + review);
            writer.newLine();

        } catch (IOException e) {
            System.out.println("Error writing rating.");
        }
    }

    /* =========================
       STATIC HELPERS
       ========================= */

    public static void saveLocationToFile(Location loc) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(DATA_DIR + "/" + loc.getName() + ".txt"))) {

            writer.write(loc.getName());
            writer.newLine();
            writer.write(loc.getDescription());
            writer.newLine();

            int[] ratings = loc.getRatings();
            String[] reviews = loc.getReviews();

            for (int i = 0; i < ratings.length; i++) {
                writer.write(ratings[i] + "|" + reviews[i]);
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving location.");
        }
    }

    public static Map<String, Location> loadLocationsFromFiles() {
        Map<String, Location> locations = new HashMap<>();
        File folder = new File(DATA_DIR);

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return locations;

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                String name = reader.readLine();
                String desc = reader.readLine();
                Location loc = new Location(name, desc);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    loc.addRating(Integer.parseInt(parts[0]), parts[1]);
                }

                locations.put(name, loc);

            } catch (Exception e) {
                System.out.println("Error loading file: " + file.getName());
            }
        }

        return locations;
    }
}
