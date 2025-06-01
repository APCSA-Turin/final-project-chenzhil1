package com.example;

import org.json.JSONObject;

public class Test { //Test class used for testing functionality of the game components
    public static void main(String[] args) {
        try {
                String urlString = "https://api.geoapify.com/v1/routing?waypoints=" + 34.1184 + "," + -118.3004 + "|" + 34.0094 + "," + -118.4973 + "&mode=" + "drive" + "&apiKey=4f2009d322ee4b4395e93c61180a2d0e";
                JSONObject jsonResponse = Map.getRequest(urlString);
                if (jsonResponse == null) {
                    System.out.println("Error: No response from API");
                    System.out.println("-1");
                }
                System.out.println(jsonResponse.getJSONArray("features").getJSONObject(0).getJSONObject("properties").getInt("time") / 60);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    
}