package com.example.diceroller.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private final SharedPreferences prefs;

    public PreferenceManager(Context context) {
        prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getAnimationDuration() {
        String durationStr = prefs.getString("pref_animation_duration", "2000");
        try {
            return Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            return 2000;
        }
    }

    public boolean isDarkMode() {
        return prefs.getBoolean("pref_dark_mode", false);
    }

    public boolean isSoundEnabled() {
        return prefs.getBoolean("pref_sound", true);
    }

    public boolean isVibrationEnabled() {
        return prefs.getBoolean("pref_vibration", true);
    }

    public int getDefaultDiceCount() {
        return prefs.getInt("pref_default_dice", 1);
    }

    public int getDefaultCardsCount() {
        return prefs.getInt("pref_default_cards", 1);
    }

    public int getDefaultNumbersCount() {
        return prefs.getInt("pref_default_numbers", 1);
    }
}
