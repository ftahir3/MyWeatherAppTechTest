package com.weatherapp.myweatherapp.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class WeatherServiceTest {

  // TODO: 12/05/2023 write unit tests
  private final WeatherService weatherService = new WeatherService();

    /**
     * tests calculation of daylight hours under normal conditions for example,
     * given the sunrise is at 06:30 and sunset is at 18:30, the expected result should be 12 hours.
     */
    @Test
    public void testCalculateDaylightHours_RegularCase() {
        double daylightHours = weatherService.calculateDaylightHours("06:30", "18:30");
        assertEquals(12.0, daylightHours, 0.1, "Daylight hours should be 12.0");
    }

    /**
     * tests calculations of daylight hours for extreme cases for example,
     * given the sunrise is at 00:00 and sunset at 23:59, the expected result should be nearly 24 hours
     */
    @Test
    public void testCalculateDaylightHours_MidnightToMidnight() {
        double daylightHours = weatherService.calculateDaylightHours("00:00", "23:59");
        assertEquals(23.98, daylightHours, 0.1, "Daylight hours should be nearly 24.0");
    }

    /**
     * tests calculations of daylight hours for a short daylight period
     * given the sunrise is at 08:00 and sunset at 10:00, the expected result should be 2 hours
     */
    @Test
    public void testCalculateDaylightHours_ShortDuration() {
        double daylightHours = weatherService.calculateDaylightHours("08:00", "10:00");
        assertEquals(2.0, daylightHours, 0.1, "Daylight hours should be 2.0");
    }

    /**
     * tests that the method will throw a NumberFormatException when its given an invalid time format.
     */
    @Test
    public void testCalculateDaylightHours_InvalidFormat() {
        Exception exception = assertThrows(NumberFormatException.class, () -> {
            weatherService.calculateDaylightHours("invalid", "time");
        });

        String expectedMessage = "For input string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage), "Should throw NumberFormatException");
    }

}