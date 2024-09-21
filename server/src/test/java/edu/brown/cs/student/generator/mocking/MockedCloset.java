package edu.brown.cs.student.generator.mocking;

import edu.brown.cs.student.main.server.clothing.enums.Category;
import edu.brown.cs.student.main.server.clothing.enums.Formality;
import edu.brown.cs.student.main.server.clothing.enums.Material;
import edu.brown.cs.student.main.server.clothing.enums.Subcategory;
import edu.brown.cs.student.main.server.clothing.records.Clothing;
import edu.brown.cs.student.main.server.clothing.records.Color;
import edu.brown.cs.student.main.server.clothing.records.Palette;
import java.util.ArrayList;

public class MockedCloset {
  public MockedCloset() {}

  public ArrayList<Clothing> getClothing(int type) {
    ArrayList<Clothing> closet = new ArrayList<>();
    closet.add(
        new Clothing(
            1,
            Category.TOP,
            Subcategory.LONG_SLEEVE,
            Formality.FORMAL,
            new Palette(new Color(1.0, 1.0, 1.0), new Color(0.0, 0.0, 0.0)),
            Material.WOOL_COTTON));

    if (type == 1) {
      closet.add(
          new Clothing(
              2,
              Category.TOP,
              Subcategory.NO_SLEEVE,
              Formality.FLEX,
              new Palette(new Color(1.0, 1.0, 1.0), new Color(1.0, 0.0, 0.0)),
              Material.STRETCHY_SPANDEX));
    }

    if (type == 1) {
      closet.add(
          new Clothing(
              3,
              Category.FULL_BODY,
              Subcategory.DRESS,
              Formality.FLEX,
              new Palette(new Color(0.0, 0.0, 0.0), new Color(0.0, 0.0, 0.0)),
              Material.SOFT_FUR));
    }

    closet.add(
        new Clothing(
            4,
            Category.SHOE,
            Subcategory.SANDAL,
            Formality.FLEX,
            new Palette(new Color(1.0, 0.0, 0.0), new Color(0.0, 0.0, 0.0)),
            Material.NOT_APPLICABLE));

    if (type == 1) {
      closet.add(
          new Clothing(
              5,
              Category.ACCESSORY,
              Subcategory.HEADWEAR,
              Formality.FLEX,
              new Palette(new Color(0.7, 0.7, 0.0), new Color(0.0, 0.0, 0.0)),
              Material.WOOL_COTTON));
    }

    if (type == 1) {
      closet.add(
          new Clothing(
              6,
              Category.OUTERWEAR,
              Subcategory.JACKET,
              Formality.FLEX,
              new Palette(new Color(0.2, 0.2, 0.2), new Color(0.0, 0.0, 0.0)),
              Material.PLASTIC_NYLON));
    }

    closet.add(
        new Clothing(
            7,
            Category.BOTTOM,
            Subcategory.PANTS,
            Formality.FLEX,
            new Palette(new Color(0.0, 0.0, 0.2), new Color(0.0, 0.0, 0.0)),
            Material.DENIM));

    if (type == 1) {
      closet.add(
          new Clothing(
              8,
              Category.OUTERWEAR,
              Subcategory.SWEATSHIRT,
              Formality.FLEX,
              new Palette(new Color(0.0, 0.0, 0.0), new Color(0.2, 0.0, 0.0)),
              Material.PLASTIC_NYLON));
    }

    if (type == 1) {
      closet.add(
          new Clothing(
              9,
              Category.ACCESSORY,
              Subcategory.SCARF,
              Formality.FLEX,
              new Palette(new Color(0.0, 0.0, 0.0), new Color(0.0, 0.2, 0.0)),
              Material.PLASTIC_NYLON));
    }

    if (type == 1) {
      closet.add(
          new Clothing(
              10,
              Category.ACCESSORY,
              Subcategory.HEADWEAR,
              Formality.FLEX,
              new Palette(new Color(0.0, 0.0, 0.0), null),
              Material.PLASTIC_NYLON));
    }

    if (type == 1) {
      closet.add(
          new Clothing(
              11,
              Category.OUTERWEAR,
              Subcategory.SWEATSHIRT,
              Formality.FLEX,
              new Palette(new Color(0.0, 0.0, 0.0), new Color(0.2, 0.2, 0.0)),
              Material.PLASTIC_NYLON));
    }

    if (type == 1) {
      closet.add(
          new Clothing(
              12,
              Category.FULL_BODY,
              Subcategory.SUIT,
              Formality.FLEX,
              new Palette(new Color(0.0, 0.0, 0.0), new Color(0.0, 0.0, 0.0)),
              Material.PLASTIC_NYLON));
    }

    if (type == 1) {
      closet.add(
          new Clothing(
              11,
              Category.TOP,
              Subcategory.NO_SLEEVE,
              Formality.FLEX,
              new Palette(new Color(0.0, 0.0, 0.0), new Color(0.0, 0.0, 0.1)),
              Material.PLASTIC_NYLON));
    }

    return closet;
  }
}
