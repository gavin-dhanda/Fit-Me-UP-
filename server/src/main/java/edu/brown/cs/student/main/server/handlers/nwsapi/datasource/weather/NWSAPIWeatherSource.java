package edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.DatasourceException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okio.Buffer;

/**
 * A datasource for weather forecasts via NWS API. This class uses the _real_ API to return results.
 * It has no caching in itself, and is focused on working with the real API.
 */
public class NWSAPIWeatherSource implements WeatherDatasource {

  private static GridResponse resolveGridCoordinates(double lat, double lon)
      throws DatasourceException {
    try {
      URL requestURL = new URL("https", "api.weather.gov", "/points/" + lat + "," + lon);
      HttpURLConnection clientConnection = connect(requestURL);
      Moshi moshi = new Moshi.Builder().build();

      JsonAdapter<GridResponse> adapter = moshi.adapter(GridResponse.class).nonNull();
      // NOTE: important! pattern for handling the input stream
      GridResponse body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
      clientConnection.disconnect();
      if (body == null || body.properties() == null || body.properties().gridId() == null)
        throw new DatasourceException("Malformed response from NWS");
      return body;
    } catch (IOException e) {
      throw new DatasourceException(e.getMessage());
    }
  }

  /**
   * Private helper method; throws IOException so different callers can handle differently if
   * needed.
   */
  private static HttpURLConnection connect(URL requestURL) throws DatasourceException, IOException {
    URLConnection urlConnection = requestURL.openConnection();
    if (!(urlConnection instanceof HttpURLConnection))
      throw new DatasourceException("unexpected: result of connection wasn't HTTP");
    HttpURLConnection clientConnection = (HttpURLConnection) urlConnection;
    clientConnection.connect(); // GET
    if (clientConnection.getResponseCode() != 200)
      throw new DatasourceException(
          "unexpected: API connection not success status: "
              + clientConnection.getResponseMessage());
    return clientConnection;
  }

  /**
   * Given a geolocation, find the current weather at that location by invoking the NWS API. This
   * method will make real web requests.
   *
   * @param loc the location to find weather at
   * @return the current weather at the given location
   * @throws DatasourceException if there is an issue obtaining the data from the API
   */
  @Override
  public WeatherData getCurrentWeather(Geolocation loc)
      throws DatasourceException, IllegalArgumentException {
    return getCurrentWeather(loc.lat(), loc.lon());
  }

  private static WeatherData getCurrentWeather(double lat, double lon)
      throws DatasourceException, IllegalArgumentException {
    try {
      // Double-check that the coordinates are valid.
      if (!Geolocation.isValidGeolocation(lat, lon)) {
        throw new IllegalArgumentException("Invalid geolocation");
      }

      // NWS is not robust to high precision; limit to X.XXXX
      lat = Math.floor(lat * 10000.0) / 10000.0;
      lon = Math.floor(lon * 10000.0) / 10000.0;

      GridResponse gridResponse = resolveGridCoordinates(lat, lon);
      String gid = gridResponse.properties().gridId();
      String gx = gridResponse.properties().gridX();
      String gy = gridResponse.properties().gridY();

      URL requestURL =
          new URL("https", "api.weather.gov", "/gridpoints/" + gid + "/" + gx + "," + gy);
      HttpURLConnection clientConnection = connect(requestURL);
      Moshi moshi = new Moshi.Builder().build();

      JsonAdapter<ForecastResponse> adapter = moshi.adapter(ForecastResponse.class).nonNull();

      ForecastResponse body =
          adapter.fromJson(new Buffer().readFrom(clientConnection.getInputStream()));

      clientConnection.disconnect();

      // Validity checks for response
      if (body == null
          || body.properties() == null
          || body.properties().maxTemperature() == null
          || body.properties().minTemperature() == null
          || body.properties().skyCover() == null
          || body.properties().probabilityOfPrecipitation() == null
          || body.properties().snowfallAmount() == null
          || body.properties().temperature() == null) {
        throw new DatasourceException("Malformed response from NWS");
      }

      List<ForecastResponseTempValue> highs = body.properties().maxTemperature().values();
      List<ForecastResponseTempValue> lows = body.properties().minTemperature().values();
      List<ForecastResponseTempValue> skyCover = body.properties().skyCover().values();
      List<ForecastResponseTempValue> pop = body.properties().probabilityOfPrecipitation().values();
      List<ForecastResponseTempValue> apparentTemp = body.properties().temperature().values();
      List<ForecastResponseTempValue> snowfallAmt = body.properties().snowfallAmount().values();

      if (highs.isEmpty()
          || lows.isEmpty()
          || skyCover.isEmpty()
          || pop.isEmpty()
          || snowfallAmt.isEmpty()
          || apparentTemp.isEmpty()) {
        throw new DatasourceException("Could not obtain weather data from NWS");
      }

      // Get time in UTC:
      ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

      int high = convertToF(highs.get(0).value());
      int low = convertToF(lows.get(0).value());
      int current = convertToF(xHrAvg(apparentTemp, 2, now));
      int rain = (int) Math.round(xHrAvg(pop, 8, now));
      int cloud = (int) Math.round(xHrAvg(skyCover, 8, now));
      int snowfall = (int) Math.round(xHrAvg(snowfallAmt, 8, now));
      String date = highs.get(0).validTime().split("T")[0];
      return new WeatherData(high, low, current, rain, cloud, snowfall, lat, lon, date);

    } catch (IOException e) {
      throw new DatasourceException(e.getMessage(), e);
    }
  }

  /**
   * Converts a temperature in Celsius to Fahrenheit.
   *
   * @param c the temperature in Celsius
   * @return the temperature in Fahrenheit
   */
  private static int convertToF(double c) {
    return (int) Math.round(c * 9.0 / 5.0 + 32.0);
  }

  /**
   * Given a list of temperature values, calculate the average over the next X hours.
   *
   * @param values the list of temperature values
   * @param hours the number of hours to average over
   * @param currentTime the current time
   * @return the average temperature over the next X hours
   */
  private static double xHrAvg(
      List<ForecastResponseTempValue> values, int hours, ZonedDateTime currentTime) {
    double sum = 0;
    int count = 0;

    for (ForecastResponseTempValue v : values) {
      String[] split = v.validTime().split("/");
      int duration = convertToHours(split[1]);
      ZonedDateTime vTime = ZonedDateTime.parse(split[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME);

      for (int i = 0; i < duration; i++) {
        if (vTime.isAfter(currentTime) && vTime.isBefore(currentTime.plusHours(hours))) {
          sum += v.value();
          count++;
        }
        vTime = vTime.plusHours(1);
      }
    }
    return count == 0 ? 0 : sum / count;
  }

  /**
   * Converts a duration string to hours.
   *
   * @param durationStr the duration string
   * @return the duration in hours
   */
  private static int convertToHours(String durationStr) {
    Pattern pattern = Pattern.compile("P(?:(\\d+)D)?T(\\d+)H");
    Matcher matcher = pattern.matcher(durationStr);
    int days = 0;
    int hours = 0;
    if (matcher.find()) {
      String dayMatch = matcher.group(1);
      String hourMatch = matcher.group(2);
      days = dayMatch != null ? Integer.parseInt(dayMatch) : 0;
      hours = Integer.parseInt(hourMatch);
    }
    // Convert days to hours and add to hours
    int totalHours = days * 24 + hours;
    return totalHours;
  }

  // //////////////////////////////////////////////////////////////
  // NWS API data classes. These must be public for Moshi.
  ////////////////////////////////////////////////////////////////

  public record GridResponse(String id, GridResponseProperties properties) {}

  public record GridResponseProperties(
      String gridId, String gridX, String gridY, String timeZone, String radarStation) {}

  public record ForecastResponse(String id, ForecastResponseProperties properties) {}

  public record ForecastResponseProperties(
      String updateTime,
      ForecastResponseTemperature maxTemperature,
      ForecastResponseTemperature minTemperature,
      ForecastResponseTemperature temperature,
      ForecastResponseTemperature skyCover,
      ForecastResponseTemperature snowfallAmount,
      ForecastResponseTemperature probabilityOfPrecipitation) {}

  public record ForecastResponseTemperature(String uom, List<ForecastResponseTempValue> values) {}

  public record ForecastResponseTempValue(String validTime, double value) {}
}
