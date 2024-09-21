/**
 * Class that represents an item's description.
 */

export class Description {
  id: string = "-1";
  desc: string = "";

  reset() {
    this.id = "-1";
    this.desc = "";
  }
}