package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Map { //Used to handle any location related functionality
    // Random location
    // Used to generate a random location in the United States
    private static double latitude;
    private static double longitude;
    private static String country = ""; // Country name
    private static String city = ""; // City name
    private static String state = ""; // State name
    private static boolean debug = true; //debug mode used to print debug information
    // Student
    private static double schoolLatitude; // Latitude of the school
    private static double schoolLongitude;  // Longitude of the school
    private static double parkLatitude; // Latitude of the park
    private static double parkLongitude; // Longitude of the park

    // School
    private static String schoolName = ""; // Name of the school
    private static String schoolAddress = "";  // Address of the school
    private static String schoolType = ""; // Indicated by "Elementary", "Middle", "High", or "College"

    public static void getMap() { // Used to get a random location in the United States
        /*
         * temp test codes
         * country = "";
         * latitude = 29.457218446441388;
         * longitude = -81.13375343709545;
         * country = findPlace(latitude, longitude);
         */
        while (country.indexOf("United States") < 0 || state.equals("")) { 
            // Loop until a valid location in the United States is found
            latitude = Math.random() * 24.4 + 24.98;
            longitude = Math.random() * 57.92 - 124.85;
            System.out.println("latitude: " + latitude);
            System.out.println("longitude: " + longitude);
            country = findPlace(latitude, longitude);
        }
        if (debug) { // Print the location details if debug mode is enabled
            System.out.println("Latitude: " + latitude);
            System.out.println("Longitude: " + longitude);
            System.out.println("Country: " + country);
            System.out.println("State: " + state);
            System.out.println("City: " + city);
        }
    }

    public static double getLatitude() {
        return latitude;
    }
    public static double getLongitude() {
        return longitude;
    }

    public static String findPlace(double latitude, double longitude) {
        // Used to find the country, state, and city based on latitude and longitude
        String country = "";
        try {
            String urlString = "https://api.bigdatacloud.net/data/reverse-geocode?latitude=" + latitude + "&longitude=" + longitude + "&localityLanguage=en&key=bdc_f7778da2781747fa9c9f12c9ba30a84b";
            JSONObject jsonResponse = getRequest(urlString);
            if (jsonResponse == null) {
                System.out.println("Error: No response from API");
                return "";
            }
            country = jsonResponse.getString("countryName");
            state = jsonResponse.getString("principalSubdivision");
            city = jsonResponse.getString("locality");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return country;
    }

    public static String getCountry() {
        return country;
    }

    public static String getCity() {
        return city;
    }

    public static String getState() {
        return state;
    }

    public static void findSchool() {
        // Used to find a school based on the latitude and longitude
        double area = 0.09;
        if (schoolType.equalsIgnoreCase("Elementary") || schoolType.equalsIgnoreCase("Middle") || schoolType.equalsIgnoreCase("High") || schoolType.equalsIgnoreCase("College")) {
            // Valid type\
        } else if (schoolType.equalsIgnoreCase("")) {
            System.out.println("No school type specified");
            return;
        } else {
            System.out.println("Invalid school type");
            return;
        }
        // If the school type is valid, proceed to find the school using the Geoapify API
        try {
            String urlString = "https://api.geoapify.com/v2/places?categories=education.school&filter=rect:" + (longitude + area) + "," + (latitude + area) + "," + (longitude - area) + "," + (latitude - area) + "&limit=20&apiKey=4f2009d322ee4b4395e93c61180a2d0e";
            JSONObject jsonResponse = getRequest(urlString);
            if (jsonResponse == null) {
                System.out.println("Error: No response from API");
                return;
            }

            JSONArray features = jsonResponse.getJSONArray("features");
            schoolHelper(features);

            int iterations = 0;
            while (schoolName.equals("") && iterations < 5) { // Retry up to 5 times to find a school
                iterations++;
                if (debug) {
                    System.out.println("No " + schoolType + " found, retrying...");
                }
                area += 0.09;

                try {
                    urlString = "https://api.geoapify.com/v2/places?categories=education.school&filter=rect:" + (longitude + area) + "," + (latitude + area) + "," + (longitude - area) + "," + (latitude - area) + "&limit=20&apiKey=4f2009d322ee4b4395e93c61180a2d0e";
                    jsonResponse = getRequest(urlString);
                    if (jsonResponse == null) {
                        System.out.println("Error: No response from API");
                        return;
                    }

                    features = jsonResponse.getJSONArray("features");
                    schoolHelper(features); //Set school info
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSchoolName() {
        return schoolName;
    }

    public static String getSchoolAddress() {
        return schoolAddress;
    }

    public static String getSchoolType() {
        return schoolType;
    }

    public static void schoolHelper(JSONArray features) { 
        // Helper method to set the school information based on the features array
        if (features.length() > 0) { //Set school info to fist school until a match school is found
            schoolName = features.getJSONObject(0).getJSONObject("properties").getString("address_line1");
            schoolAddress = features.getJSONObject(0).getJSONObject("properties").getString("address_line2");
            schoolLatitude = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);
            schoolLongitude = features.getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
            for (int i = 0; i < features.length(); i++) {
                if(debug) { //Debug mode prints the name of each school found
                    System.out.println("!!!" + features.getJSONObject(i).getJSONObject("properties").getString("address_line1"));
                }
                if (features.getJSONObject(i).getJSONObject("properties").getString("address_line1").indexOf(schoolType) > 0 ) {
                    //Set school info if the school type matches
                    schoolName = features.getJSONObject(i).getJSONObject("properties").getString("address_line1");
                    schoolAddress = features.getJSONObject(i).getJSONObject("properties").getString("address_line2");
                    schoolLatitude = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);
                    schoolLongitude = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
                    System.out.println("schoolLatitude: " + schoolLatitude);
                    System.out.println("schoolLongitude: " + schoolLongitude);
                    
                    break;
                }
            }
        }
    }

    public static void setSchoolType(String type) {
        schoolType = type;
    }

    public static int getTransitTime(double lat1, double long1, double lat2, double long2, String mode) {
        //Find transit time between two locations using the Geoapify API
        if(mode.equalsIgnoreCase("transit") || mode.equalsIgnoreCase("drive") || mode.equalsIgnoreCase("walk") || mode.equalsIgnoreCase("bike")) {
                        
            try {
                String urlString = "https://api.geoapify.com/v1/routing?waypoints=" + lat1 + "," + long1 + "|" + lat2 + "," + long2 + "&mode=" + mode + "&apiKey=4f2009d322ee4b4395e93c61180a2d0e";
                JSONObject jsonResponse = getRequest(urlString);
                if (jsonResponse == null) {
                    System.out.println("Error: No response from API");
                    return -1;
                }
                return jsonResponse.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getInt("time") / 60;

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
    public static double getSchoolLatitude() {
        return schoolLatitude;
    }
    public static double getSchoolLongitude() {
        return schoolLongitude;
    }

    

    public static JSONObject getRequest(String urlString) {
        // Used to make a GET request to the specified URL and return the JSON response
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
            }
            in.close();
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (debug) {
            System.out.println(jsonResponse);
            }
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void findPark() {
        while(parkLatitude == 0 && parkLongitude == 0) { // Loop until a valid park is found
            double area = 0.09;
            try {
                String urlString = "https://api.geoapify.com/v2/places?categories=leisure.park&filter=rect:" + (longitude + area) + "," + (latitude + area) + "," + (longitude - area) + "," + (latitude - area) + "&limit=20&apiKey=4f2009d322ee4b4395e93c61180a2d0e";
                JSONObject jsonResponse = getRequest(urlString);
                if (jsonResponse == null) {
                    System.out.println("Error: No response from API");
                    return;
                }
                JSONArray features = jsonResponse.getJSONArray("features");
                if (features.length() > 0) {
                    for(int i = 0; i < features.length(); i++) {
                        JSONObject properties = features.getJSONObject(i).getJSONObject("properties");
                        if (properties.getString("address_line1").toLowerCase().contains("park")) {
                            // Set park info if the name contains "park"
                            parkLatitude = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(1);
                            parkLongitude = features.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(0);
                            break;
                        }
                    }
                    System.out.println("Park Latitude: " + parkLatitude);
                    System.out.println("Park Longitude: " + parkLongitude);
                } else {
                    System.out.println("No parks found, retrying...");
                    area += 0.09; // Increase the search area
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static double getParkLatitude() {
        return parkLatitude;
    }
    public static double getParkLongitude() {
        return parkLongitude;
    }

}
