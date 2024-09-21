package edu.brown.cs.student.main.server.clothing.generation;

import edu.brown.cs.student.main.server.clothing.enums.Category;
import edu.brown.cs.student.main.server.clothing.enums.Formality;
import edu.brown.cs.student.main.server.clothing.records.Clothing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/** This class stores all the clothing items in a hashmap. */
public class ClosetData {

  private HashMap<Category, HashMap<Formality, HashMap<Integer, Clothing>>> closet;
  private ArrayList<Clothing> clothingList; // Contains all the clothing

  /**
   * Constructor for the closet data.
   *
   * @param clothingList is the list of clothing items.
   */
  public ClosetData(ArrayList<Clothing> clothingList) {
    this.clothingList = clothingList;
    this.initializeCloset();
    this.loadCloset();
  }

  /** Initialize all the hashmaps. */
  private void initializeCloset() {
    this.closet = new HashMap<>();

    this.closet.put(Category.TOP, new HashMap<>());
    this.closet.put(Category.BOTTOM, new HashMap<>());
    this.closet.put(Category.SHOE, new HashMap<>());
    this.closet.put(Category.FULL_BODY, new HashMap<>());
    this.closet.put(Category.OUTERWEAR, new HashMap<>());
    this.closet.put(Category.ACCESSORY, new HashMap<>());

    for (HashMap<Formality, HashMap<Integer, Clothing>> formalityMap : this.closet.values()) {
      formalityMap.put(Formality.FORMAL, new HashMap<>());
      formalityMap.put(Formality.INFORMAL, new HashMap<>());
      formalityMap.put(Formality.FLEX, new HashMap<>());
    }
  }

  /** Places all the clothing items into their hashmaps. */
  private void loadCloset() {
    for (Clothing item : this.clothingList) {
      this.closet.get(item.category()).get(item.formality()).put(item.id(), item);

      if (item.formality() == Formality.FLEX) {
        this.closet.get(item.category()).get(Formality.INFORMAL).put(item.id(), item);
        this.closet.get(item.category()).get(Formality.FORMAL).put(item.id(), item);
      }
    }
  }

  /**
   * This method randomly selects roughly half of the items in a category.
   *
   * @param formality is the specified formality.
   * @param category is the specified category.
   * @return a list of the items.
   */
  public ArrayList<Clothing> getRandItem(Formality formality, Category category) {
    ArrayList<Clothing> randList = new ArrayList<>();
    HashMap<Integer, Clothing> items = this.closet.get(category).get(formality);

    // Number of items in closet
    int numItems = items.size();

    if (numItems > 0) {

      double n = (double) numItems * 1.5;

      for (int i = 0; i < n; i++) {
        Random random = new Random();
        int randomIndex = random.nextInt(numItems);

        // Go through the items until you get to the random index
        int counter = 0;
        for (Clothing item : items.values()) {
          if (counter == randomIndex) {
            randList.add(item);
          }
          counter++;
        }
      }
    }
    return randList;
  }

  /**
   * Returns a ratio of the number of full body items to the number of full body items and tops
   * combined.
   *
   * @param formality is the formality to consider.
   * @return a ratio of full body to full body plus tops.
   */
  public double hasFullBody(Formality formality) {
    double numFull = this.closet.get(Category.FULL_BODY).get(formality).size();
    double numTops = this.closet.get(Category.TOP).get(formality).size();
    return (numFull / (numTops + numFull));
  }
}
