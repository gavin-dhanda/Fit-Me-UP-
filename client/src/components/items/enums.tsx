/**
 * Various enums used for our project.
 */

export enum PageType {
  Generate = "generate",
  Saved = "saved",
  Home = "home",
  Closet = "closet",
}

export enum WeatherType {
  SNOW = "snow",
  CLOUDY = "cloudy",
  RAINY = "rainy",
  CLEAR = "clear",
}

export enum Category {
    Top = 0,
    Bottom = 1,
    Shoe = 2,
    FullBody = 3,
    Outerwear = 4,
    Accessory = 5,
}

export enum Subcategory {
    LongSleeve = 0, 
    ShortSleeve = 1,
    NoSleeve = 2,
    Skirt = 3,
    Pants = 4,
    Shorts = 5,
    Sneaker = 6,
    Boot = 7,
    Sandal = 8,
    Dress = 9,
    Suit = 10,
    Romper = 11,
    Sweatshirt = 12,
    Jacket = 13,
    Cardigan = 14,
    Headwear = 15,
    Scarf = 16,
    Bag = 17,
}

export enum Material {
    WoolCotton = 0,
    PlasticNylon = 1,
    Leather = 2,
    Denim = 3,
    SoftFur = 4,
    StretchySpandex = 5,
    Other = 6,
}

export enum Formality {
    Formal = 0,
    Informal = 1,
    Flex = 2,
}