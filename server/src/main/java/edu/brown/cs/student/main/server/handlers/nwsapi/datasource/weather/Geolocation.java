package edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather;

/**
 * A record to hold a specific geolocation in lat/lon coordinates.
 *
 * @param lat latitude in degrees
 * @param lon longitude in degrees
 */
public record Geolocation(double lat, double lon) {

  /**
   * You can provide a custom constructor for records. We do so here for input-validity checking.
   * Note the lack of arguments! They are defined by the record declaration itself, above.
   */
  public Geolocation {
    if (!Geolocation.isValidGeolocation(lat, lon)) {
      throw new IllegalArgumentException("Invalid geolocation");
    }
  }

  /**
   * Static validity checker for geocoordinates. Since lat and lon are in degrees, we expect them to
   * fall into a particular range. If they do not, the coordinate is invalid.
   *
   * @return true if and only if this is a valid coordinate pair
   */
  public static boolean isValidGeolocation(double lat, double lon) {
    return lat >= -90 && lat <= 90 && lon >= -180 && lon <= 180;
  }
}
