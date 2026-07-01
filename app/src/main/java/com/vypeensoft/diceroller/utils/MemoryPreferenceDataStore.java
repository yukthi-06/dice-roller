package com.vypeensoft.diceroller.utils;

import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceDataStore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MemoryPreferenceDataStore extends PreferenceDataStore {

    private final SharedPreferences realPrefs;
    private final Map<String, Object> changes = new HashMap<>();

    public MemoryPreferenceDataStore(SharedPreferences realPrefs) {
        this.realPrefs = realPrefs;
    }

    @Override
    public void putString(String key, @Nullable String value) {
        changes.put(key, value);
    }

    @Override
    public void putStringSet(String key, @Nullable Set<String> values) {
        changes.put(key, values);
    }

    @Override
    public void putInt(String key, int value) {
        changes.put(key, value);
    }

    @Override
    public void putLong(String key, long value) {
        changes.put(key, value);
    }

    @Override
    public void putFloat(String key, float value) {
        changes.put(key, value);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        changes.put(key, value);
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        if (changes.containsKey(key)) return (String) changes.get(key);
        return realPrefs.getString(key, defValue);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        if (changes.containsKey(key)) return (Set<String>) changes.get(key);
        return realPrefs.getStringSet(key, defValues);
    }

    @Override
    public int getInt(String key, int defValue) {
        if (changes.containsKey(key)) return (Integer) changes.get(key);
        return realPrefs.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        if (changes.containsKey(key)) return (Long) changes.get(key);
        return realPrefs.getLong(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        if (changes.containsKey(key)) return (Float) changes.get(key);
        return realPrefs.getFloat(key, defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        if (changes.containsKey(key)) return (Boolean) changes.get(key);
        return realPrefs.getBoolean(key, defValue);
    }

    public void saveToRealPrefs() {
        if (changes.isEmpty()) return;
        
        SharedPreferences.Editor editor = realPrefs.edit();
        for (Map.Entry<String, Object> entry : changes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (Float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else if (value instanceof Set) {
                try {
                    editor.putStringSet(key, (Set<String>) value);
                } catch (ClassCastException ignored) {}
            }
        }
        editor.apply();
        changes.clear();
    }
}
