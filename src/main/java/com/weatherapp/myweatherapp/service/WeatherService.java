package com.weatherapp.myweatherapp.service;


import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  @Value("${weather.visualcrossing.key}")
  private String apiKey;

  private final String API_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";

  private final RestTemplate restTemplate = new RestTemplate();

  public CityInfo forecastByCity(String city) {

    return weatherRepo.getByCity(city);
  }

  /** retrieves the sunrise and sunset times for a given city and calculates the total daylight hours.
   *
   * @param city , the city name for which daylight hours will be calculated
   * @return , number of daylight hours for the given city
   */
  public double getDaylightHours(String city) {
    try {
      String url = API_URL + city + "?key=" + apiKey;
      String response = restTemplate.getForObject(url, String.class);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonResponse = objectMapper.readTree(response);
      JsonNode today = jsonResponse.get("days").get(0);

      String sunrise = today.get("sunrise").asText();
      String sunset = today.get("sunset").asText();

      return calculateDaylightHours(sunrise, sunset);
    } catch (IOException e) {
      throw new RuntimeException("Error parsing JSON response", e);
    }
  }

  /** checks if its currently raining in a given city by checking the weather condition
   *
   * @param city , the city for which it'll check the weather status
   * @return , true if its raining, and false if it's not raining
   */
  public boolean isRaining(String city) {
    try {
      String url = API_URL + city + "?key=" + apiKey;
      String response = restTemplate.getForObject(url, String.class);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonResponse = objectMapper.readTree(response);
      JsonNode today = jsonResponse.get("days").get(0);
      String conditions = today.get("conditions").asText();

      return conditions.toLowerCase().contains("rain");
    } catch (IOException e) {
      throw new RuntimeException("Error parsing JSON response", e);
    }
  }

  /**
   * calculates the daylight hours by using the sunrise and sunset times.
   * @param sunrise , the sunrise time
   * @param sunset , the sunset time
   * @return , the total number of daylight hours
   */
  public double calculateDaylightHours(String sunrise, String sunset) {
    String[] sunriseParts = sunrise.split(":");
    String[] sunsetParts = sunset.split(":");

    double sunriseHour = Double.parseDouble(sunriseParts[0]) + (Double.parseDouble(sunriseParts[1]) / 60);
    double sunsetHour = Double.parseDouble(sunsetParts[0]) + (Double.parseDouble(sunsetParts[1]) / 60);

    return sunsetHour - sunriseHour;
  }


}
