package edu.brown.cs.student.main.server.clothing.enums;

/** Enum for subcategories */
public enum Subcategory {
  LONG_SLEEVE(0, 60.0),
  SHORT_SLEEVE(1, 70.0),
  NO_SLEEVE(2, 70.0),
  SKIRT(3, 70.0),
  PANTS(4, 60.0),
  SHORTS(5, 65.0),
  SNEAKER(6, -1.0),
  BOOT(7, -1.0),
  SANDAL(8, 70.0),
  DRESS(9, 70.0),
  SUIT(10, 60.0),
  ROMPER(11, 60.0),
  SWEATSHIRT(12, 50.0),
  JACKET(13, 30.0),
  CARDIGAN(14, 40.0),
  HEADWEAR(15, -1.0),
  SCARF(16, 30.0),
  BAG(17, 60.0);

  private final int index;
  private final double weather;

  Subcategory(int id, double weather) {
    this.index = id;
    this.weather = weather;
  }

  /**
   * Returns the index of the subcategory.
   *
   * @return the index of the subcategory
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * The ideal temperature for the item, -1 means doesn't matter.
   *
   * @return the weather value.
   */
  public double getWeather() {
    return this.weather;
  }
}
