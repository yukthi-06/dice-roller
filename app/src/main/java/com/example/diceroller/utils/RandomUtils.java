package com.example.diceroller.utils;

import com.example.diceroller.models.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomUtils {
    private static final Random random = new Random();
    private static final String[] SUITS = {"♠", "♥", "♦", "♣"};
    private static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public static int getRandomDiceFace() {
        return random.nextInt(6) + 1; // 1-6
    }

    private static final String[] DICTIONARY = {
        "Apple", "Banana", "Cat", "Dog", "Elephant", "Frog", "Giraffe", "Horse", "Ice", "Jungle",
        "Kangaroo", "Lion", "Monkey", "Nest", "Orange", "Penguin", "Queen", "Rabbit", "Sun", "Tree",
        "Umbrella", "Violet", "Water", "Xylophone", "Yellow", "Zebra", "Bird", "Car", "Desk", "Fish",
        "Goat", "Hat", "Igloo", "Juice", "Kite", "Lamp", "Mouse", "Nut", "Owl", "Pig",
        "Quilt", "Rose", "Star", "Train", "Unicorn", "Van", "Whale", "Yacht", "Zip", "Ant",
        "Bear", "Cow", "Duck", "Eagle", "Fox", "Guitar", "House", "Island", "Jacket", "Key"
    };

    public static int getRandomNumber(int max) {
        return random.nextInt(max) + 1; // 1-max
    }

    public static char getRandomAlphabet() {
        return (char) ('A' + random.nextInt(26));
    }

    public static String getRandomWord() {
        return DICTIONARY[random.nextInt(DICTIONARY.length)];
    }

    public static List<Card> getStandardDeck() {
        List<Card> deck = new ArrayList<>();
        for (String suit : SUITS) {
            boolean isRed = suit.equals("♥") || suit.equals("♦");
            for (String rank : RANKS) {
                deck.add(new Card(suit, rank, isRed));
            }
        }
        return deck;
    }

    public static List<Card> pickRandomCards(int count) {
        List<Card> deck = getStandardDeck();
        Collections.shuffle(deck);
        return deck.subList(0, Math.min(count, deck.size()));
    }
}
