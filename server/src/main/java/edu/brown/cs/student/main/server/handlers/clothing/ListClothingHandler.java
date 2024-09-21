package edu.brown.cs.student.main.server.handlers.clothing;

import edu.brown.cs.student.main.server.clothing.records.Clothing;
import edu.brown.cs.student.main.server.handlers.Utils;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * ListClothingHandler is called by the list-clothing endpoint in server, and works to retrieve the
 * list of clothing associated with the user who created them.
 */
public class ListClothingHandler implements Route {

  public StorageInterface storageHandler;

  public ListClothingHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
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
      String uid = request.queryParams("uid");

      // Get all the clothing items for the user
      List<Map<String, Object>> vals = this.storageHandler.getCollection(uid, "clothing");

      // Convert the key,value map to just a list of the clothing items as hash maps.
      List<String> clothingList =
          vals.stream().map(clothing -> clothing.get("clothing").toString()).toList();
      List<Clothing> clothingConverted =
          clothingList.stream().map(Utils::fromStringClothing).toList();
      List<Map<String, String>> clothingMaps =
          clothingConverted.stream().map(Utils::clothingToHashMap).toList();

      // Get all of the clothing descriptions
      List<Map<String, Object>> descriptions =
          this.storageHandler.getCollection(uid, "clothing-description");

      // Convert to list of Strings
      List<String> descriptionList =
          descriptions.stream()
              .map(description -> description.get("description").toString())
              .toList();

      List<Map<String, String>> descriptionMap = new ArrayList<>();
      for (String description : descriptionList) {
        String[] parts = description.split(",");
        Map<String, String> clothingDescription = new HashMap<>();
        clothingDescription.put("id", parts[1].split("-")[1]);
        clothingDescription.put("desc", parts[0]);
        descriptionMap.add(clothingDescription);
      }

      responseMap.put("response_type", "success");
      responseMap.put("clothing", clothingMaps);
      responseMap.put("descriptions", descriptionMap);
    } catch (Exception e) {
      e.printStackTrace();
      // Error likely occurred in the storage handler.
      responseMap.put("response_type", "error");
      String[] parts = e.getClass().toString().split("\\.");
      responseMap.put("exception", parts[parts.length - 1]);
      responseMap.put("error_message", e.getMessage());
    }

    return Utils.toMoshiJson(responseMap);
  }
}
