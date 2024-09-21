package edu.brown.cs.student.compatability;

import edu.brown.cs.student.main.server.clothing.enums.Category;
import edu.brown.cs.student.main.server.clothing.enums.Formality;
import edu.brown.cs.student.main.server.clothing.enums.Material;
import edu.brown.cs.student.main.server.clothing.enums.Subcategory;
import edu.brown.cs.student.main.server.clothing.generation.CompatibilityUtils;
import edu.brown.cs.student.main.server.clothing.records.Clothing;
import edu.brown.cs.student.main.server.clothing.records.Color;
import edu.brown.cs.student.main.server.clothing.records.Palette;
import edu.brown.cs.student.main.server.handlers.nwsapi.datasource.weather.WeatherData;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class TestCompatability {

  @Test
  public void TestBaseCompat() {
    Color pink = new Color(1.0, 0.6, 1.0);
    Color lightRed = new Color(1.0, 0.6, 0.6);
    Color darkGreen = new Color(0.05, 0.3, 0.05);

    CompatibilityUtils comp = new CompatibilityUtils();

    Assert.assertTrue(comp.colorDif(pink, lightRed) > comp.colorDif(pink, darkGreen));
  }

  @Test
  public void TestValueCompat() {
    Color pink = new Color(1.0, 0.6, 1.0);
    Color lightRed = new Color(1.0, 0.6, 0.6);
    Color darkGreen = new Color(0.05, 0.3, 0.05);

    CompatibilityUtils comp = new CompatibilityUtils();

    Assert.assertTrue(comp.shadeDif(pink, lightRed) > comp.shadeDif(pink, darkGreen));
  }

  @Test
  public void TestWeatherComp() {
    WeatherData cold = new WeatherData(0, 0, 0, 0, 0, 0, 0, 0, "");
    WeatherData warm = new WeatherData(100, 100, 100, 100, 100, 100, 100, 100, "");

    Clothing scarf =
        new Clothing(
            9,
            Category.ACCESSORY,
            Subcategory.SCARF,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.WOOL_COTTON);

    Clothing hat =
        new Clothing(
            9,
            Category.ACCESSORY,
            Subcategory.HEADWEAR,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.WOOL_COTTON);

    Clothing tanktop =
        new Clothing(
            9,
            Category.TOP,
            Subcategory.NO_SLEEVE,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.WOOL_COTTON);

    Clothing longsleeve =
        new Clothing(
            9,
            Category.TOP,
            Subcategory.LONG_SLEEVE,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.WOOL_COTTON);

    Clothing jacket =
        new Clothing(
            9,
            Category.OUTERWEAR,
            Subcategory.JACKET,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.PLASTIC_NYLON);

    Clothing cardigan =
        new Clothing(
            9,
            Category.OUTERWEAR,
            Subcategory.CARDIGAN,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.PLASTIC_NYLON);

    Clothing sandals =
        new Clothing(
            9,
            Category.SHOE,
            Subcategory.SANDAL,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.PLASTIC_NYLON);

    Clothing sneakers =
        new Clothing(
            9,
            Category.SHOE,
            Subcategory.SNEAKER,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.PLASTIC_NYLON);

    CompatibilityUtils comp = new CompatibilityUtils();

    Assert.assertTrue(comp.weatherComp(jacket, cold) > comp.weatherComp(cardigan, cold));
    Assert.assertTrue(comp.weatherComp(jacket, warm) < comp.weatherComp(cardigan, warm));

    Assert.assertTrue(comp.weatherComp(sneakers, cold) > comp.weatherComp(sandals, cold));
    Assert.assertTrue(comp.weatherComp(sneakers, warm) < comp.weatherComp(sandals, warm));

    Assert.assertTrue(comp.weatherComp(longsleeve, cold) > comp.weatherComp(tanktop, cold));
    Assert.assertTrue(comp.weatherComp(longsleeve, warm) < comp.weatherComp(tanktop, warm));

    Assert.assertTrue(comp.weatherComp(scarf, cold) > comp.weatherComp(hat, cold));
    Assert.assertTrue(comp.weatherComp(scarf, warm) < comp.weatherComp(hat, warm));
  }

  @Test
  public void TestMaterialComp() {
    Clothing leather =
        new Clothing(
            9,
            Category.TOP,
            Subcategory.LONG_SLEEVE,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.LEATHER);

    Clothing cotton =
        new Clothing(
            9,
            Category.TOP,
            Subcategory.LONG_SLEEVE,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.WOOL_COTTON);

    Clothing stretch =
        new Clothing(
            9,
            Category.TOP,
            Subcategory.LONG_SLEEVE,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.0), null),
            Material.STRETCHY_SPANDEX);

    CompatibilityUtils comp = new CompatibilityUtils();

    ArrayList<Clothing> stretchs = new ArrayList<>();
    stretchs.add(stretch);

    Assert.assertTrue(comp.materialComp(cotton, stretchs) > comp.materialComp(leather, stretchs));
  }
}
