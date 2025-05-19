package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

public class Map {
    // Random location
    private static double latitude;
    private static double longitude;
    private static String country = "";
    private static String city = "";
    private static String state = "";
    private static boolean debug = true;

    // School
    private static String schoolName = "";
    private static String schoolAddress = "";
    private static String schoolType = ""; // Indicated by "Elementary", "Middle", "High", or "College"

    public static void getMap() {
        /*
         * temp test codes
         * country = "";
         * latitude = 29.457218446441388;
         * longitude = -81.13375343709545;
         * country = findPlace(latitude, longitude);
         */
        while (country.indexOf("United States") < 0 || state.equals("")) {
            latitude = Math.random() * 24.4 + 24.98;
            longitude = Math.random() * 57.92 - 124.85;
            System.out.println("latitude: " + latitude);
            System.out.println("longitude: " + longitude);
            country = findPlace(latitude, longitude);
        }
        if (debug) {
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
        String country = "";
        try {
            String urlString = "https://api.bigdatacloud.net/data/reverse-geocode?latitude=" + latitude + "&longitude=" + longitude + "&localityLanguage=en&key=bdc_f7778da2781747fa9c9f12c9ba30a84b";
            JSONObject jsonResponse = getRequest(urlString);
            if (jsonResponse == null) {
                System.out.println("Error: No response from API");
                return "";
            }
            if (debug) {
                System.out.println(jsonResponse);
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

        try {
            String urlString = "https://api.geoapify.com/v2/places?categories=education.school&filter=rect:" + (longitude + area) + "," + (latitude + area) + "," + (longitude - area) + "," + (latitude - area) + "&limit=20&apiKey=4f2009d322ee4b4395e93c61180a2d0e";
            JSONObject jsonResponse = getRequest(urlString);
            if (jsonResponse == null) {
                System.out.println("Error: No response from API");
                return;
            }
            if (debug) {
                System.out.println(jsonResponse);
            }

            JSONArray features = jsonResponse.getJSONArray("features");
            schoolHelper(features);

            int iterations = 0;
            while (schoolName.equals("") && iterations < 5) {
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
                    if (debug) {
                        System.out.println(jsonResponse);
                    }

                    features = jsonResponse.getJSONArray("features");
                    schoolHelper(features);
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
        if (features.length() > 0) {
            
            for (int i = 0; i < features.length(); i++) {
                if(debug) {
                    System.out.println("!!!" + features.getJSONObject(i).getJSONObject("properties").getString("address_line1"));
                }
                if (features.getJSONObject(i).getJSONObject("properties").getString("address_line1").indexOf(schoolType) > 0 ) {
                    schoolName = features.getJSONObject(i).getJSONObject("properties").getString("address_line1");
                    schoolAddress = features.getJSONObject(i).getJSONObject("properties").getString("address_line2");
                    break;
                }
            }
        }
    }

    public static void setSchoolType(String type) {
        schoolType = type;
    }

    public static int getTransitTime(double lat1, double long1, double lat2, double long2, String mode) {
        if(mode.equalsIgnoreCase("transit")) {
                        
            try {
                String urlString = "https://api.geoapify.com/v1/routing?waypoints=" + lat1 + "," + long1 + "|" + lat2 + "," + long2 + "&mode=" + mode + "&apiKey=4f2009d322ee4b4395e93c61180a2d0e";
                JSONObject jsonResponse = getRequest(urlString);
                if (jsonResponse == null) {
                    System.out.println("Error: No response from API");
                    return -1;
                }
                if (debug) {
                    System.out.println(jsonResponse);
                }

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static JSONObject getRequest(String urlString) {
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
}
