package edu.brown.cs.student.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.handlers.mocking.MockedWeather;
import edu.brown.cs.student.main.server.handlers.ClearUserHandler;
import edu.brown.cs.student.main.server.handlers.clothing.AddClothingHandler;
import edu.brown.cs.student.main.server.handlers.clothing.ListClothingHandler;
import edu.brown.cs.student.main.server.handlers.clothing.RemoveClothingHandler;
import edu.brown.cs.student.main.server.handlers.nwsapi.WeatherHandler;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherDatasource;
import edu.brown.cs.student.main.server.handlers.outfits.AddOutfitHandler;
import edu.brown.cs.student.main.server.handlers.outfits.GenerateOutfitHandler;
import edu.brown.cs.student.main.server.handlers.outfits.ListOutfitsHandler;
import edu.brown.cs.student.main.server.handlers.outfits.RemoveOutfitHandler;
import edu.brown.cs.student.main.server.storage.FirebaseUtilities;
import edu.brown.cs.student.main.server.storage.StorageInterface;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;
import spark.Spark;

public class TestHandlers {

  public TestHandlers() throws IOException {}

  /** Set up the server port. */
  @BeforeClass
  public static void setupOnce() {
    // Pick an arbitrary free port
    Spark.port(0);
    // Eliminate logger spam in console for test suite
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root
  }

  // Helping Moshi serialize Json responses.
  private final Type mapStringObject =
      Types.newParameterizedType(Map.class, String.class, Object.class);
  private JsonAdapter<Map<String, Object>> adapter;
  private final StorageInterface firebaseUtils = new FirebaseUtilities();

  /**
   * Helper to start a connection to a specific API endpoint/params
   *
   * <p>The "throws" clause doesn't matter below -- JUnit will fail if an exception is thrown that
   * hasn't been declared as a parameter to @Test.
   *
   * @param apiCall the call string, including endpoint (Note: this would be better if it had more
   *     structure!)
   * @return the connection for the given URL, just after connecting
   * @throws IOException if the connection fails for some reason
   */
  private HttpURLConnection tryRequest(String apiCall) throws IOException {
    // Configure the connection (but don't actually send a request yet)
    URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
    HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
    // The request body contains a Json object
    clientConnection.setRequestProperty("Content-Type", "application/json");
    // We're expecting a Json object in the response body
    clientConnection.setRequestProperty("Accept", "application/json");

    clientConnection.connect();
    return clientConnection;
  }

  /**
   * Joint test for clear, add, list, and remove clothing handlers.
   *
   * @throws IOException
   */
  @Test
  public void TestClearAddListRemoveClothingHandler() throws IOException {
    /////////////////// CLEAR CLOTHING ///////////////////

    Spark.get("clear-user", new ClearUserHandler(this.firebaseUtils));
    Spark.awaitInitialization(); // don't continue until the server is listening

    // New Moshi adapter for responses and requests
    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);

    // Set up the request, make the request
    HttpURLConnection loadConnection = tryRequest("clear-user?uid=1");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    Map<String, Object> responseBody =
        adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("clear-user");
    Spark.awaitStop(); // don't proceed until the server is stopped

    /////////////////// ADD CLOTHING ///////////////////

    Spark.get("add-clothing", new AddClothingHandler(this.firebaseUtils));
    Spark.awaitInitialization(); // don't continue until the server is listening

    // Set up the request, make the request
    loadConnection =
        tryRequest(
            "add-clothing?uid=1&category=1&subcategory=1&formality=1&material=1&primary=%23aabb16&secondary=%23ffffff&material=1&description=hello");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    responseBody = adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("add-clothing");
    Spark.awaitStop(); // don't proceed until the server is stopped

    /////////////////// LIST CLOTHING ///////////////////

    Spark.get("list-clothing", new ListClothingHandler(this.firebaseUtils));
    Spark.awaitInitialization(); // don't continue until the server is listening

    // Set up the request, make the request
    loadConnection = tryRequest("list-clothing?uid=1");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    responseBody = adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("list-clothing");
    Spark.awaitStop(); // don't proceed until the server is stopped

    /////////////////// REMOVE CLOTHING ///////////////////

    Spark.get("remove-clothing", new RemoveClothingHandler(this.firebaseUtils));
    Spark.awaitInitialization(); // don't continue until the server is listening

    // Set up the request, make the request
    loadConnection = tryRequest("remove-clothing?uid=1&id=0");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    responseBody = adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("remove-clothing");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Test the weather handler with mocking.
   *
   * @throws IOException
   */
  @Test
  public void TestWeatherHandler() throws IOException {
    WeatherDatasource weatherSource = new MockedWeather();
    Spark.get("weather", new WeatherHandler(weatherSource));
    Spark.awaitInitialization(); // don't continue until the server is listening

    // New Moshi adapter for responses and requests
    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);

    // Set up the request, make the request
    HttpURLConnection loadConnection = tryRequest("weather?lat=10&lon=10");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    Map<String, Object> responseBody =
        adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("weather");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Integrated test for generating, adding, listing, and removing an outfit with handlers.
   *
   * @throws IOException
   */
  @Test
  public void TestGenerateAddListRemoveOutfitHandler() throws IOException {
    WeatherDatasource weatherSource = new MockedWeather();

    Spark.get("add-outfit", new AddOutfitHandler(this.firebaseUtils));
    Spark.get("list-outfits", new ListOutfitsHandler(this.firebaseUtils));
    Spark.get("remove-outfit", new RemoveOutfitHandler(this.firebaseUtils));
    Spark.get("generate-outfit", new GenerateOutfitHandler(this.firebaseUtils, weatherSource));

    Spark.awaitInitialization();

    // New Moshi adapter for responses and requests
    Moshi moshi = new Moshi.Builder().build();
    adapter = moshi.adapter(mapStringObject);

    ///////////// GENERATE OUTFIT /////////////

    // Set up the request, make the request
    HttpURLConnection loadConnection =
        tryRequest("generate-outfit?uid=1&formality=1&lat=10&lon=10");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    Map<String, Object> responseBody =
        adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    ///////////// ADD OUTFIT /////////////

    // Set up the request, make the request
    loadConnection =
        tryRequest("add-outfit?uid=1&top=0&bottom=0&shoe=0&accessory=-1&outerwear=-1&fullbody=-1");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    responseBody = adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    ///////////// LIST OUTFIT /////////////

    // Set up the request, make the request
    loadConnection = tryRequest("list-outfits?uid=1");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    responseBody = adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    ///////////// REMOVE OUTFIT /////////////

    // Set up the request, make the request
    loadConnection = tryRequest("remove-outfit?uid=1&id=0");
    // Get an OK response (the *connection* worked, the *API* provides an error response)
    assertEquals(200, loadConnection.getResponseCode());
    // Get the expected response: a success
    responseBody = adapter.fromJson(new Buffer().readFrom(loadConnection.getInputStream()));
    assertEquals("success", responseBody.get("response_type"));

    loadConnection.disconnect();

    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("add-outfit");
    Spark.unmap("list-outfits");
    Spark.unmap("remove-outfit");
    Spark.unmap("generate-outfit");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }
}
