package com.vypeensoft.diceroller.models;

public class Card {
    private final String suit;
    private final String rank;
    private final boolean isRed;
    private boolean isFaceUp;

    public Card(String suit, String rank, boolean isRed) {
        this.suit = suit;
        this.rank = rank;
        this.isRed = isRed;
        this.isFaceUp = false;
    }

    public String getSuit() { return suit; }
    public String getRank() { return rank; }
    public boolean isRed() { return isRed; }
    public boolean isFaceUp() { return isFaceUp; }
    public void setFaceUp(boolean faceUp) { isFaceUp = faceUp; }

    public String getDisplayString() {
        return rank + suit;
    }
}
