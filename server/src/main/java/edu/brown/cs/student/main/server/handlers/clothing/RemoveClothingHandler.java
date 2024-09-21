package edu.brown.cs.student.main.server.handlers.clothing;

import edu.brown.cs.student.main.server.clothing.records.Clothing;
import edu.brown.cs.student.main.server.handlers.Utils;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/** This class handles removing a clothing item. */
public class RemoveClothingHandler implements Route {

  private StorageInterface storageHandler;

  /**
   * This constructor initializes a RemoveClothingHandler object.
   *
   * @param storageHandler the storage handler
   */
  public RemoveClothingHandler(StorageInterface storageHandler) {
    this.storageHandler = storageHandler;
  }

  /**
   * This method handles the request to remove a clothing item.
   *
   * @param request the request
   * @param response the response
   * @return the response
   */
  @Override
  public Object handle(Request request, Response response) {
    Map<String, Object> responseMap = new HashMap<>();
    try {
      String uid = request.queryParams("uid");
      String id = request.queryParams("id");

      this.storageHandler.deleteDocument(
          this.storageHandler.getDocumentReference(uid, "clothing", "clothing-" + id));
      this.storageHandler.deleteDocument(
          this.storageHandler.getDocumentReference(uid, "clothing-description", "clothing-" + id));

      // Get all outfits, and remove any outfits with the clothing item.
      List<Map<String, Object>> outfits = this.storageHandler.getCollection(uid, "outfits");
      List<String> outfitStrings =
          outfits.stream().map(outfit -> outfit.get("outfit").toString()).toList();

      for (String outfit : outfitStrings) {
        // Split the outfit string, and remove outfit if it includes clothing item.
        String[] parts = outfit.split(",");
        for (int i = 1; i < parts.length; i++) {
          if (parts[i].equals(id)) {
            this.storageHandler.deleteDocument(
                this.storageHandler.getDocumentReference(uid, "outfits", "outfit-" + parts[0]));
            break;
          }
        }
      }

      List<Map<String, Object>> vals = this.storageHandler.getCollection(uid, "clothing");

      List<String> clothingList =
          vals.stream().map(clothing -> clothing.get("clothing").toString()).toList();
      List<Clothing> clothingConverted =
          clothingList.stream().map(Utils::fromStringClothing).toList();
      List<Map<String, String>> clothingMaps =
          clothingConverted.stream().map(Utils::clothingToHashMap).toList();

      responseMap.put("response_type", "success");
      responseMap.put("clothing", clothingMaps);

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
