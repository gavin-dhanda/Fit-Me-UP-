package edu.brown.cs.student.main.server.handlers.nwsapi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.Geolocation;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherData;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherDatasource;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class handles weather requests. */
public class WeatherHandler implements Route {
  // Internal datasource (note using interface; might be NWS, might be something else)
  private final WeatherDatasource state;

  /**
   * Accepts a weather handler via dependency injection. The handler need not know or care what kind
   * of WeatherDatasource we give it.
   *
   * @param state
   */
  public WeatherHandler(WeatherDatasource state) {
    this.state = state;
  }

  /**
   * This method handles the request to get the weather.
   *
   * @param request the request
   * @param response the response
   * @return the response
   */
  @Override
  public Object handle(Request request, Response response) {
    // Step 1: Prepare to send a reply of some sort
    Moshi moshi = new Moshi.Builder().build();
    Type mapStringObject = Types.newParameterizedType(Map.class, String.class, Object.class);
    JsonAdapter<Map<String, Object>> adapter = moshi.adapter(mapStringObject);
    Map<String, Object> responseMap = new HashMap<>();

    // Get the location that the request is for
    String lat = request.queryParams("lat");
    String lon = request.queryParams("lon");
    if (lat == null || lon == null) {
      // Bad request! Send an error response.
      responseMap.put("query_lon", lon);
      responseMap.put("query_lat", lat);
      responseMap.put("type", "error");
      responseMap.put("error_type", "missing_parameter");
      responseMap.put("error_arg", lat == null ? "lat" : "lon");
      return adapter.toJson(responseMap);
    }

    // Generate the reply
    try {

      double lat_double = Double.parseDouble(lat);
      double lon_double = Double.parseDouble(lon);
      Geolocation loc = new Geolocation(lat_double, lon_double);
      WeatherData data = state.getCurrentWeather(loc);
      responseMap.put("response_type", "success");
      responseMap.put("temperature", data);

      return adapter.toJson(responseMap);
    } catch (Exception e) {
      responseMap.put("query_lon", lon);
      responseMap.put("query_lat", lat);
      responseMap.put("response_type", "error");
      String[] parts = e.getClass().toString().split("\\.");
      responseMap.put("exception", parts[parts.length - 1]);
      responseMap.put("error_message", e.getMessage());
      return adapter.toJson(responseMap);
    }
  }
}
