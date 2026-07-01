package com.vypeensoft.diceroller.utils;

import com.vypeensoft.diceroller.models.Card;
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

    private static final List<String> DICTIONARY = new ArrayList<>();

    public static void initDictionary(android.content.Context context) {
        if (!DICTIONARY.isEmpty()) return;
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(context.getAssets().open("words.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    DICTIONARY.add(line);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            DICTIONARY.add("Error");
        }
    }

    public static int getRandomNumber(int max) {
        return random.nextInt(max) + 1; // 1-max
    }

    public static char getRandomAlphabet() {
        return (char) ('A' + random.nextInt(26));
    }

    public static String getRandomWord() {
        if (DICTIONARY.isEmpty()) return "Error";
        return DICTIONARY.get(random.nextInt(DICTIONARY.size()));
    }

    public static String getRandomGibberishWord() {
        int length = random.nextInt(5) + 4; // 4 to 8 characters
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26)));
        }
        return sb.toString();
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
