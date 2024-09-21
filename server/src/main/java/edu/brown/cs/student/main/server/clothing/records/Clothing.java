package edu.brown.cs.student.main.server.clothing.records;

import edu.brown.cs.student.main.server.clothing.enums.Category;
import edu.brown.cs.student.main.server.clothing.enums.Formality;
import edu.brown.cs.student.main.server.clothing.enums.Material;
import edu.brown.cs.student.main.server.clothing.enums.Subcategory;

/** This class represents a clothing item. */
public record Clothing(
    int id,
    Category category,
    Subcategory subcategory,
    Formality formality,
    Palette colors,
    Material material) {}
