import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class WeatherApiClient {

    // Replace with your OpenWeatherMap API key
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        String city = "London";
        String response = getWeatherData(city);

        if (response != null) {
            parseAndDisplayWeather(response);
        } else {
            System.out.println("Failed to get weather data.");
        }
    }

    /**
     * Makes an HTTP GET request to the weather API.
     */
    private static String getWeatherData(String city) {
        StringBuilder response = new StringBuilder();

        try {
            String urlString = BASE_URL + "?q=" + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int status = connection.getResponseCode();
            System.out.println("HTTP Response Code: " + status);

            if (status == 200) { // OK
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } else {
                System.out.println("Error: " + status);
                return null;
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response.toString();
    }

    /**
     * Parses JSON response and displays data.
     */
    private static void parseAndDisplayWeather(String jsonResponse) {
        JSONObject json = new JSONObject(jsonResponse);

        String cityName = json.getString("name");
        JSONObject main = json.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");

        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("---- Weather Data ----");
        System.out.println("City: " + cityName);
        System.out.println("Temperature: " + temp + "°C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Conditions: " + description);
    }
}
