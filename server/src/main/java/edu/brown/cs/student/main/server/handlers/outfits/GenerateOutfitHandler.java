package edu.brown.cs.student.main.server.handlers.outfits;

import edu.brown.cs.student.main.server.clothing.enums.Formality;
import edu.brown.cs.student.main.server.clothing.generation.ClosetData;
import edu.brown.cs.student.main.server.clothing.generation.Generator;
import edu.brown.cs.student.main.server.clothing.records.Clothing;
import edu.brown.cs.student.main.server.clothing.records.Outfit;
import edu.brown.cs.student.main.server.handlers.Utils;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.Geolocation;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherData;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherDatasource;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** GenerateOutfitHandler is called to generate a new outfit for the user. */
public class GenerateOutfitHandler implements Route {

  private StorageInterface storageHandler;
  private WeatherDatasource weatherDatasource;

  /**
   * Constructor for the GenerateOutfitHandler.
   *
   * @param storageHandler The storage handler to be used.
   * @param weatherDatasource The weather datasource to be used.
   */
  public GenerateOutfitHandler(
      StorageInterface storageHandler, WeatherDatasource weatherDatasource) {
    this.storageHandler = storageHandler;
    this.weatherDatasource = weatherDatasource;
  }

  /**
   * Invoked when a request is made on this route's corresponding path
   *
   * @param request The request object providing information about the HTTP request
   * @param response The response object providing functionality for modifying the response
   * @return The content to be set in the response
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      // Collect parameters from the request to build an outfit.
      String uid = request.queryParams("uid");
      int formality = Integer.parseInt(request.queryParams("formality"));
      double lat = Double.parseDouble(request.queryParams("lat"));
      double lon = Double.parseDouble(request.queryParams("lon"));

      // Get all the clothing items for the user
      List<Map<String, Object>> vals = this.storageHandler.getCollection(uid, "clothing");
      // Convert the key,value map to just a list of the clothing items.
      List<String> clothingList =
          vals.stream().map(clothing -> clothing.get("clothing").toString()).toList();
      //
      List<Clothing> clothingConverted =
          clothingList.stream().map(Utils::fromStringClothing).toList();
      ArrayList<Clothing> closet = new ArrayList<>(clothingConverted);

      Generator generator = new Generator(new ClosetData(closet));
      Formality formalityEnum = Formality.values()[formality];
      WeatherData weatherData = weatherDatasource.getCurrentWeather(new Geolocation(lat, lon));
      Outfit outfit = generator.generateOutfit(weatherData, formalityEnum);

      responseMap.put("response_type", "success");
      responseMap.put("outfit", Utils.serializeOutfit(outfit, "0"));
    } catch (Exception e) {
      // Error likely occurred in the storage handler.
      responseMap.put("response_type", "error");
      String[] parts = e.getClass().toString().split("\\.");
      responseMap.put("exception", parts[parts.length - 1]);
      responseMap.put("error_message", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}
