package com.example;

public class Test { //Test class used for testing functionality of the game components
    public static void main(String[] args) {
        // Set a known location (e.g., Central Park, New York)
        // Example: latitude = 40.785091, longitude = -73.968285
        // You may need to make Map's latitude/longitude setters public or set them via getMap()
        Map.getMap(); // Or set manually if you want a specific location

        Map.findPark();

        System.out.println("Park Latitude: " + Map.getParkLatitude());
        System.out.println("Park Longitude: " + Map.getParkLongitude());
    }
}