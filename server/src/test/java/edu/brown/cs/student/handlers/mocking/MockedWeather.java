package edu.brown.cs.student.handlers.mocking;

import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.Geolocation;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherData;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherDatasource;

public class MockedWeather implements WeatherDatasource {

  public WeatherData getCurrentWeather(Geolocation loc) throws IllegalArgumentException {
    return new WeatherData(0, 0, 0, 0, 0, 0, 0, 0, "");
  }
}
