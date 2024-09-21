package edu.brown.cs.student.main.server.clothing.records;

/** This class represents an outfit. */
public record Outfit(
    Clothing top,
    Clothing bottom,
    Clothing shoe,
    Clothing outerwear,
    Clothing fullbody,
    Clothing accessory) {}
