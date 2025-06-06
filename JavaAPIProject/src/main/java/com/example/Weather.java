package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.io.IOException;
public class Weather {
    private static double[] temperature = new double[24];
    private static boolean debug = false;
    private static int[] weather = new int[24];
    public static void findWeather() {
        //Find weather using Open Meteo API from the latitude and longitude of the map
        try {
            String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + Map.getLatitude() + "&longitude=" + Map.getLongitude() + "&current_weather=true&hourly=temperature_2m,weathercode";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                if(debug) {
                    System.out.println(jsonResponse);
                }
                for (int i = 0; i < 24; i++) {
                    temperature[i] = jsonResponse.getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(i);
                    weather[i] = jsonResponse.getJSONObject("hourly").getJSONArray("weathercode").getInt(i);
                }
            } else {
                System.out.println("GET request failed: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String defineWeather(int wCode) {
        //Define weather based on the weather code
        String weather = "";
        switch (wCode) {
            case 0:
                weather = "Clear sky";
                break;
            case 1:
                weather = "Mainly clear";
                break;
            case 2:
                weather = "Partly cloudy";
                break;
            case 3:
                weather = "Overcast";
                break;
            case 45:
                weather = "Fog";
                break;
            case 48:
                weather = "Depositing rime fog";
                break;
            case 51:
                weather = "Drizzle: Light intensity";
                break;
            case 53:
                weather = "Drizzle: Moderate intensity";
                break;
            case 55:
                weather = "Drizzle: Dense intensity";
                break;
            case 56:
                weather = "Freezing Drizzle: Light intensity";
                break;
            case 57:
                weather = "Freezing Drizzle: Dense intensity";
                break;
            case 61:
                weather = "Rain: Slight intensity";
                break;
            case 63:
                weather = "Rain: Moderate intensity";
                break;
            case 65:
                weather = "Rain: Heavy intensity";
                break;
            case 66:
                weather = "Freezing Rain: Light intensity";
                break;
            case 67:
                weather = "Freezing Rain: Heavy intensity";
                break;
            case 71:
                weather = "Snow fall: Slight intensity";
                break;
            case 73:
                weather = "Snow fall: Moderate intensity";
                break;
            case 75:
                weather = "Snow fall: Heavy intensity";
                break;
            case 77:
                weather = "Snow grains";
                break;
            case 80:
                weather = "Rain showers: Slight intensity";
                break;
            case 81:
                weather = "Rain showers: Moderate or heavy intensity";
                break;
            case 82:
                weather = "Rain showers: Violent";
                break;
            case 85:
                weather = "Snow showers: Slight";
                break;
            case 86:
                weather = "Snow showers: Heavy";
                break;
            case 95:    
                weather = "Thunderstorm: Slight or moderate";
                break;
            case 96:
                weather = "Thunderstorm with slight hail";
                break;
            case 99:
                weather = "Thunderstorm with heavy hail";
                break;
            default:
                weather = "Unknown weather code";
                break; 
        }
        return weather;
    }

    public static double getTemperature(int hour) {
        return temperature[hour];
    }
    public static int getWeather(int hour) {
        return weather[hour];
    }
}
