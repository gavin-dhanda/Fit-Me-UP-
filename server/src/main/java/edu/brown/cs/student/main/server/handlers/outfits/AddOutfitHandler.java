package edu.brown.cs.student.main.server.handlers.outfits;

import com.google.cloud.firestore.DocumentReference;
import edu.brown.cs.student.main.server.handlers.Utils;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * AddOutfitHandler is called by the add outfit endpoint in server, and works to add an outfit to
 * the user's closet.
 */
public class AddOutfitHandler implements Route {

  public StorageInterface storageHandler;

  /**
   * Constructor for the AddOutfitHandler.
   *
   * @param storageHandler The storage handler to be used.
   */
  public AddOutfitHandler(StorageInterface storageHandler) {
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
      String topID = request.queryParams("top");
      String bottomID = request.queryParams("bottom");
      String shoeID = request.queryParams("shoe");
      String outerwearID = request.queryParams("outerwear");
      String fullbodyID = request.queryParams("fullbody");
      String accessoryID = request.queryParams("accessory");

      // Try and get the next outfit ID, but if it doesnt exits, use 0.
      String id;
      try {
        DocumentReference userIDs =
            this.storageHandler.getDocumentReference(uid, "userIDs", "outfitID");
        Map<String, Object> userIDsMap = this.storageHandler.getDocument(userIDs);
        id = userIDsMap.get("nextID").toString();
      } catch (Exception e) {
        id = "0";
      }

      // Update the next outfit ID in the database.
      Map<String, Object> outfitData = new HashMap<>();
      outfitData.put("nextID", Integer.toString((Integer.parseInt(id) + 1)));
      this.storageHandler.addDocument(uid, "userIDs", "outfitID", outfitData);

      Map<String, Object> data = new HashMap<>();
      // Create comma separated ID list:
      String outfit =
          id
              + ","
              + topID
              + ","
              + bottomID
              + ","
              + shoeID
              + ","
              + outerwearID
              + ","
              + fullbodyID
              + ","
              + accessoryID;
      data.put("outfit", outfit);
      String outfitID = "outfit-" + id;

      // Use the storage handler to add the document to the database.
      this.storageHandler.addDocument(uid, "outfits", outfitID, data);

      responseMap.put("response_type", "success");
      responseMap.put("outfit", Utils.outfitStringToHashMap(outfit));
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
