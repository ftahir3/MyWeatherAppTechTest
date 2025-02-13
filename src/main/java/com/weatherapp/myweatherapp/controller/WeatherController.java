package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/weather")
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo ci = weatherService.forecastByCity(city);

    return ResponseEntity.ok(ci);
  }

  // TODO: given two city names, compare the length of the daylight hours and return the city with the longest day

  /** The daylight hours are calculated by using the sunrise and sunset times that are fetched from the visual crossing API
   *
   * @param city1 ,is the first city for comparison
   * @param city2 , is the second city for comparison
   * @return , a string indicating which city has longer daylight hours
   */
  @GetMapping("/compare-daylight")
  public ResponseEntity<String> compareDaylight(@RequestParam String city1, @RequestParam String city2) {
    try {
      double daylightHoursCity1 = weatherService.getDaylightHours(city1);
      double daylightHoursCity2 = weatherService.getDaylightHours(city2);

      String result;
      if (daylightHoursCity1 > daylightHoursCity2) {
        result = city1 + " has longer daylight hours.";
      } else if (daylightHoursCity1 < daylightHoursCity2) {
        result = city2 + " has longer daylight hours.";
      } else {
        result = "Both cities have the same daylight hours.";
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
  }

  // TODO: given two city names, check which city its currently raining in

  /** given two cities, it fetches information from the visual crossing API to check which of the two cities is currently experiencing rain
   *
   * @param city1 , is the first city for rain check
   * @param city2, is the second city for rain check
   * @return , a string indicating which city is experiencing rain
   */
  @GetMapping("/rain-check")
  public ResponseEntity<String> checkRain(@RequestParam String city1, @RequestParam String city2) {
    try {
      boolean isRainingCity1 = weatherService.isRaining(city1);
      boolean isRainingCity2 = weatherService.isRaining(city2);

      String result;
      if (isRainingCity1 && isRainingCity2) {
        result = "Both cities are experiencing rain.";
      } else if (isRainingCity1) {
        result = city1 + " is experiencing rain.";
      } else if (isRainingCity2) {
        result = city2 + " is experiencing rain.";
      } else {
        result = "Neither city is experiencing rain.";
      }

      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Error: " + e.getMessage());
    }
  }

}
