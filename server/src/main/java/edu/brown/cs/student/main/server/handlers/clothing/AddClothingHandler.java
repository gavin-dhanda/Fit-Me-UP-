package edu.brown.cs.student.main.server.handlers.clothing;

import com.google.cloud.firestore.DocumentReference;
import edu.brown.cs.student.main.server.handlers.Utils;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * AddClothingHandler is called by the add clothing endpoint in server, and works to add an item of
 * clothing to the user's closet.
 */
public class AddClothingHandler implements Route {

  public StorageInterface storageHandler;

  public AddClothingHandler(StorageInterface storageHandler) {
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
      // Collect parameters from the request to build a clothing item.
      String uid = request.queryParams("uid");
      String category = request.queryParams("category");
      String subcategory = request.queryParams("subcategory");
      String formality = request.queryParams("formality");
      String material = request.queryParams("material");
      String primary = request.queryParams("primary");
      String secondary = request.queryParams("secondary");
      String description = request.queryParams("description");

      // Try and get the next clothing ID, but if it doesnt exits, use 0.
      String id;
      try {
        DocumentReference userIDs =
            this.storageHandler.getDocumentReference(uid, "userIDs", "clothingID");
        Map<String, Object> userIDsMap = this.storageHandler.getDocument(userIDs);
        id = userIDsMap.get("nextID").toString();
      } catch (Exception e) {
        id = "0";
      }

      // Update the next clothing ID in the database.
      Map<String, Object> clothingData = new HashMap<>();
      clothingData.put("nextID", Integer.toString((Integer.parseInt(id) + 1)));
      this.storageHandler.addDocument(uid, "userIDs", "clothingID", clothingData);

      Map<String, Object> data = new HashMap<>();
      String clothing =
          id
              + ","
              + category
              + ","
              + subcategory
              + ","
              + formality
              + ","
              + primary
              + ","
              + secondary
              + ","
              + material;

      data.put("clothing", clothing);
      String clothingId = "clothing-" + id;

      // Use the storage handler to add the document to the database.
      this.storageHandler.addDocument(uid, "clothing", clothingId, data);

      // Add the description to firebase separately.
      HashMap<String, Object> descriptionData = new HashMap<>();
      descriptionData.put("description", description + "," + clothingId);
      this.storageHandler.addDocument(uid, "clothing-description", clothingId, descriptionData);

      responseMap.put("response_type", "success");
      responseMap.put("clothing", Utils.clothingToHashMap(Utils.fromStringClothing(clothing)));
      responseMap.put("description", description);
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
