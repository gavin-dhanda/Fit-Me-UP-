/**
 * An object that represents a clothing item.
 */

export class ClothingItem {
  id : number = -1;
  category: number = -1;
  subcategory : number = -1;
  primary: string = "";
  secondary: string = "null";
  material: number = -1;
  formality: number = -1;

  reset() {
    this.id = -1;
    this.category = -1;
    this.subcategory = -1;
    this.primary = "";
    this.secondary = "null";
    this.material = -1;
    this.formality = -1;
  }
}
