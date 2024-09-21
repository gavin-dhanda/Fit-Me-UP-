package edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather;

/**
 * A record representing a weather-report datum, or rather the information we want to retain from a
 * weather report and provide to our client(s).
 *
 * @param high the high temperature for the day in Fahrenheit
 * @param low the low temperature for the day in Fahrenheit
 * @param current the current temperature in Fahrenheit
 * @param rain the average chance of rain for the next 8 hours
 * @param cloud the amount of cloud cover for the next 8 hours
 * @param snowfall the amount of snowfall expected in mm
 * @param lat the latitude of the location
 * @param lon the longitude of the location
 * @param date the date of the weather report
 */
public record WeatherData(
    int high,
    int low,
    int current,
    int rain,
    int cloud,
    int snowfall,
    double lat,
    double lon,
    String date) {}
