package edu.brown.cs.student.main.server.clothing.generation;

import edu.brown.cs.student.main.server.clothing.records.Clothing;
import edu.brown.cs.student.main.server.clothing.records.Color;
import edu.brown.cs.student.main.server.clothing.records.CompatibilityPair;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherData;
import java.util.ArrayList;

/** This class contains the compatibility functions for the clothing items. */
public class CompatibilityUtils {

  /** Constructor for the compatibility utils. */
  public CompatibilityUtils() {}

  /**
   * Algorithmically decide the best item out of the options given the existing items and the
   * weather data.
   *
   * @param options is the set of items to pick from.
   * @param existing is the list of current items.
   * @param weather is the current weather.
   * @return the best item.
   */
  public CompatibilityPair pickBest(
      ArrayList<Clothing> options, ArrayList<Clothing> existing, WeatherData weather) {
    // Default to the first option
    Clothing best = options.get(0);

    // Initialize the current compatibility scores
    double bestScore = 0;
    double currScore;

    // Get compatibility for all options, updating the best one
    for (Clothing option : options) {
      if ((currScore = this.getCompatibility(option, existing, weather)) > bestScore) {
        bestScore = currScore;
        best = option;
      }
    }

    return new CompatibilityPair(best, bestScore);
  }

  /**
   * Uses helper functions to get weather, material, and color compatibility, and returns a weighted
   * average.
   *
   * @param option is the item to test.
   * @param existing is the existing items.
   * @param weather is the current weather.
   * @return a compatibility value between 0 and 10.
   */
  private double getCompatibility(
      Clothing option, ArrayList<Clothing> existing, WeatherData weather) {

    // All values will be between zero and one
    double weatherComp = this.weatherComp(option, weather);
    double materialComp = this.materialComp(option, existing);
    double colorComp = this.colorComp(option, existing);

    // Return a weighted average
    return (4.0 * weatherComp) + materialComp + (5.0 * colorComp);
  }

  //////////////// WEATHER COMPATIBILITY ////////////////

  /**
   * Calculates how compatible the item is with the current weather.
   *
   * @param option is the item to test.
   * @param weatherData is the given weather stats.
   * @return a compatibility value between zero and 1.
   */
  public double weatherComp(Clothing option, WeatherData weatherData) {
    if (option.subcategory().getWeather() == -1.0) {
      return 0.8;
    }

    double temp =
        ((double) (weatherData.high() + weatherData.low() + 2.0 * weatherData.current())) / 4.0;

    temp = Math.max(30.0, temp);
    temp = Math.min(temp, 70.0);
    temp = temp / 100.0;

    double itemVal = option.subcategory().getWeather() / 100.0;

    return 1.0 - (Math.abs(temp - itemVal));
  }

  //////////////// MATERIAL COMPATIBILITY ////////////////

  /**
   * Calculates the material compatibility with an existing list.
   *
   * @param option is the item to test.
   * @param existing is the list to test against.
   * @return the compatibility of material from 0 to 1.
   */
  public double materialComp(Clothing option, ArrayList<Clothing> existing) {
    double n = existing.size();

    if (n == 0) {
      return 1;
    }

    double agg = 0;
    for (Clothing test : existing) {
      agg += option.material().compatWith(test.material());
    }
    return (agg / n);
  }

  //////////////// COLOR COMPATIBILITY ////////////////

  /**
   * Calculates the color compatibility between an item of clothing and an existing set.
   *
   * @param option is the clothing to test.
   * @param existing is the existing set.
   * @return a value from zero to 1 for compatibility.
   */
  private double colorComp(Clothing option, ArrayList<Clothing> existing) {

    ArrayList<Color> existingColors = getColors(existing);
    Color colorOne = option.colors().primary();
    Color colorTwo = option.colors().accent();

    int numColors = numColors(existingColors);
    int numShades = numShades(existingColors);

    if (numColors == 1 && numShades == 1) {
      // If the color is compatible the shade doesn't matter, if the shade is compatible, the color
      // doesn't matter.
      // So prioritize color.
      double primary = colorCompat(colorOne, existingColors);

      if (colorTwo == null) {
        return primary;
      } else {
        double secondary = colorCompat(colorTwo, existingColors);
        return (0.7 * primary) + (0.3 * secondary);
      }
    } else if (numColors > 1) {
      // Need to be compatible with the shade
      double primary =
          (shadeCompat(colorOne, existingColors) + colorCompat(colorOne, existingColors)) / 2.0;

      if (colorTwo == null) {
        return primary;
      } else {
        double secondary =
            (shadeCompat(colorTwo, existingColors) + shadeCompat(colorTwo, existingColors)) / 2.0;
        return (0.7 * primary) + (0.3 * secondary);
      }
    } else { // numShades > 1
      // Need to be compatible with color
      double primary = colorCompat(colorOne, existingColors);

      if (colorTwo == null) {
        return primary;
      } else {
        double secondary = colorCompat(colorTwo, existingColors);
        return (0.7 * primary) + (0.3 * secondary);
      }
    }
  }

  /**
   * Gets the approximate number of unique colors.
   *
   * @param colors is the full set of colors.
   * @return the number of unique ones.
   */
  private int numColors(ArrayList<Color> colors) {
    ArrayList<Color> uniques = new ArrayList<>();

    for (Color color : colors) {
      // If it's the first color add it
      if (uniques.isEmpty()) {
        uniques.add(color);
      } else {
        // Get the color value of the current one
        Color true1 = trueColor(color);

        // Default it to being unique
        boolean isUnique = true;

        // If it's similar to any of the current unique colors, it isn't unique
        for (Color unique : uniques) {
          Color true2 = trueColor(unique);
          double diff =
              Math.abs(true1.r() - true2.r())
                  + Math.abs(true1.g() - true2.g())
                  + Math.abs(true1.b() - true2.b());
          if (diff < 0.2) {
            isUnique = false;
          }
        }
        if (isUnique) {
          uniques.add(color);
        }
      }
    }
    return uniques.size();
  }

  /**
   * Returns the approximate number of different Shades in the list.
   *
   * @param colors is the list of colors.
   * @return the number of Shades.
   */
  private int numShades(ArrayList<Color> colors) {
    ArrayList<Color> uniques = new ArrayList<>();

    for (Color color : colors) {
      // If it's the first color add it
      if (uniques.isEmpty()) {
        uniques.add(color);
      } else {
        // Get the color value of the current one
        double whiteness1 = whiteness(color);
        double blackness1 = blackness(color);

        // Default it to being unique
        boolean isUnique = true;

        // If it's similar to any of the current unique colors, it isn't unique
        for (Color unique : uniques) {
          double whiteness2 = whiteness(unique);
          double blackness2 = blackness(unique);

          // Calculate difference in whiteness and blackness
          double diffW = Math.abs(whiteness1 - whiteness2);
          double diffB = Math.abs(blackness1 - blackness2);

          if (diffW < 0.1 && diffB < 0.1) {
            isUnique = false;
          }
        }
        if (isUnique) {
          uniques.add(color);
        }
      }
    }
    return uniques.size();
  }

  /**
   * Return a list of the individual colors in the list of clothing items.
   *
   * @param items is a list of clothes.
   * @return a list of colors.
   */
  private ArrayList<Color> getColors(ArrayList<Clothing> items) {
    ArrayList<Color> colors = new ArrayList<>();

    for (Clothing item : items) {
      if (item.colors().primary() != null) {
        colors.add(item.colors().primary());
      }
      if (item.colors().accent() != null) {
        colors.add(item.colors().primary());
      }
    }

    return colors;
  }

  /**
   * Gets the compatibility of a color with a set of colors in terms of hue.
   *
   * @param color is the color to be tested.
   * @param existing are the colors to test against.
   * @return the compatibility average.
   */
  private double colorCompat(Color color, ArrayList<Color> existing) {
    double num = existing.size();
    double sum = 0.0;

    for (Color exist : existing) {
      double dif = colorDif(color, exist);

      // if the compatibility is less than 0.5, give a penalty
      if (dif < 0.6) {
        dif = -1;
      }
      sum += dif;
    }
    return sum / num;
  }

  /**
   * Gets the compatibility of a color with a set of colors in terms of shade.
   *
   * @param color is the color to test.
   * @param existing are the existing colors.
   * @return the average compatibility, plus a penalty.
   */
  private double shadeCompat(Color color, ArrayList<Color> existing) {
    double num = existing.size();
    double sum = 0.0;

    for (Color exist : existing) {
      double dif = shadeDif(color, exist);

      // if the compatibility is less than 0.5, give a penalty
      if (dif < 0.5) {
        dif = -0.5;
      }
      sum += dif;
    }
    return sum / num;
  }

  /**
   * Finds the color compatibility between two hues.
   *
   * @param one is first color.
   * @param two is second color.
   * @return the compatibility.
   */
  public double colorDif(Color one, Color two) {
    Color true1 = trueColor(one);
    Color true2 = trueColor(two);

    double diff =
        Math.abs(true1.r() - true2.r())
            + Math.abs(true1.g() - true2.g())
            + Math.abs(true1.b() - true2.b());
    return 1.0 - diff;
  }

  /**
   * Finds the compatibility between two shades.
   *
   * @param one is first color.
   * @param two is second color.
   * @return the compatibility.
   */
  public double shadeDif(Color one, Color two) {
    double whiteness1 = whiteness(one);
    double blackness1 = blackness(one);
    double whiteness2 = whiteness(two);
    double blackness2 = blackness(two);

    // Calculate difference in whiteness and blackness
    double diffW = Math.abs(whiteness1 - whiteness2);
    double diffB = Math.abs(blackness1 - blackness2);

    return 1.0 - diffB - diffW;
  }

  /**
   * Normalized the color to extract its true value.
   *
   * @param color is the original color.
   * @return the true color without shade.
   */
  private Color trueColor(Color color) {
    double r = color.r();
    double g = color.g();
    double b = color.b();

    double max = Math.max(Math.max(color.r(), color.b()), color.g());
    double min = Math.min(Math.min(color.r(), color.b()), color.g());
    double range = max - min;

    if (range == 0) {
      return new Color(r, g, b);
    }

    double tr = (r - min) / range;
    double tg = (g - min) / range;
    double tb = (b - min) / range;

    return new Color(tr, tg, tb);
  }

  /**
   * Calculates a higher number for less white values, and a number close to zero for white values.
   *
   * @param color is the original color.
   * @return the variance of r, g, b.
   */
  private double whiteness(Color color) {
    double r = color.r();
    double g = color.g();
    double b = color.b();

    double mean = (r + g + b) / 3.0;

    double max = Math.max(Math.max(color.r(), color.b()), color.g());
    double min = Math.min(Math.min(color.r(), color.b()), color.g());
    double range = max - min;

    return (range / mean) / 3; // Max value is 3, so will be 0 to 1
  }

  /**
   * Calculates the rough blackness of an image. A number close to zero for black values.
   *
   * @param color is the original color.
   * @return the maximum value of r, g, and b.
   */
  private double blackness(Color color) {
    return Math.max(Math.max(color.r(), color.b()), color.g());
  }
}
