/**
 * Class that represents an outfit.
 */

export class OutfitItem {
  top: string = "-1";
  bottom: string = "-1";
  outerwear: string = "-1";
  id: string = "-1";
  fullbody: string = "-1";
  accessory: string = "-1";
  shoe: string = "-1";

  reset() {
    this.top = "-1";
    this.bottom = "-1";
    this.outerwear = "-1";
    this.id = "-1";
    this.fullbody = "-1";
    this.accessory = "-1";
    this.shoe = "-1";
  }
}