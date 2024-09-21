import {
  boots,
  cardigan,
  dress,
  dresspants,
  jacket,
  jeans,
  jeanshorts,
  longsleeve,
  romper,
  shorts,
  skirt,
  sneakers,
  scarf,
  shortsleeve,
  buttondown,
  sweatpants,
  nosleeve,
  hat,
  bag,
  sandals,
  suit,
  sweatshirt,
} from "../icons/clothes/clothes";
import {
  Subcategory,
  Formality,
  Category,
  Material,
} from "../components/items/enums";

/**
 * Funcion that determines image of bottom.
 * @param shape the shape of the bottom.
 * @param material the material of the bottom.
 * @param formality the formality of the bottom.
 * @returns appropriate image to use.
 */
function determineBottom(shape: number, material: number, formality: number) {
  switch (shape?.toString()) {
    case Subcategory.Skirt.toString():
      return skirt;
    case Subcategory.Pants.toString():
      if (formality.toString() == Formality.Formal.toString()) {
        return dresspants;
      } else {
        if (
          material.toString() == Material.Denim.toString() ||
          material.toString() == Material.Leather.toString()
        ) {
          return jeans;
        } else {
          return sweatpants;
        }
      }
    case Subcategory.Shorts.toString():
      if (
        material.toString() == Material.Denim.toString() ||
        material.toString() == Material.Leather.toString()
      ) {
        return jeanshorts;
      } else {
        return shorts;
      }
  }
}

/**
 * Funcion that determines image of top.
 * @param subcategory the subcategory of the top.
 * @param formality the formality of the top.
 * @returns appropriate image to use.
 */
function determineTOP(
  subcategory: number,
  formality: number
) {
  switch (subcategory.toString()) {
    case Subcategory.LongSleeve.toString():
      if (formality.toString() == Formality.Formal.toString()) {
        return buttondown;
      } else {
        return longsleeve;
      }
    case Subcategory.ShortSleeve.toString():
      return shortsleeve;
    case Subcategory.NoSleeve.toString():
      return nosleeve;
  }
}

/**
 * Funcion that determines image of the fullbody item.
 * @param subcategory the subcategory of the fullbody item.
 * @returns appropriate image to use.
 */
function determineFullBody(subcategory: number) {
  switch (subcategory.toString()) {
    case Subcategory.Dress.toString():
      return dress;
    case Subcategory.Suit.toString():
      return suit;
    case Subcategory.Romper.toString():
      return romper;
  }
}

/**
 * Funcion that determines image of the shoe.
 * @param subcategory the subcategory of the shoe.
 * @returns appropriate image to use.
 */
function determineShoe(subcategory: number) {
  switch (subcategory.toString()) {
    case Subcategory.Sneaker.toString():
      return sneakers;
    case Subcategory.Boot.toString():
      return boots;
    case Subcategory.Sandal.toString():
      return sandals;
  }
}

/**
 * Funcion that determines image of the outerwear item.
 * @param subcategory the subcategory of the outerwear item.
 * @returns appropriate image to use.
 */
function determineOuterwear(subcategory: number) {
  switch (subcategory.toString()) {
    case Subcategory.Sweatshirt.toString():
      return sweatshirt;
    case Subcategory.Jacket.toString():
      return jacket;
    case Subcategory.Cardigan.toString():
      return cardigan;
  }
}

/**
 * Funcion that determines image of the accessory item.
 * @param subcategory the subcategory of the accesory item.
 * @returns appropriate image to use.
 */
function determineAccessory(subcategory: number) {
  switch (subcategory.toString()) {
    case Subcategory.Headwear.toString():
      return hat;
    case Subcategory.Scarf.toString():
      return scarf;
    case Subcategory.Bag.toString():
      return bag;
  }
}

/**
 * Function that calls on helper functions to get the appropriate image.
 * @param category the category of the item.
 * @param subcategory the subcategory of the item.
 * @param material the material of the item.
 * @param formality the formality of the item.
 * @returns the image to use.
 */
export function determineCategory(
  category: number,
  subcategory: number,
  material: number,
  formality: number
) {
  switch (category.toString()) {
    case Category.Top.toString():
      return determineTOP(subcategory, material);
    case Category.Bottom.toString():
      return determineBottom(subcategory, material, formality);
    case Category.Shoe.toString():
      return determineShoe(subcategory);
    case Category.FullBody.toString():
      return determineFullBody(subcategory);
    case Category.Outerwear.toString():
      return determineOuterwear(subcategory);
    case Category.Accessory.toString():
      return determineAccessory(subcategory);
    default:
      console.log("Unknown or undefined category:", category);
  }
}
