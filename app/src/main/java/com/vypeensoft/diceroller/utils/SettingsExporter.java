package com.vypeensoft.diceroller.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class SettingsExporter {

    private static final String TAG = "SettingsExporter";
    private static final String EXPORT_DIR = "Vypeensoft/Dice_Roller/settings";
    private static final String EXPORT_FILE_NAME = "settings.json";

    public static void exportSettingsToJson(Context context) {
        // Check if we can write to external storage (skip if not)
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "External storage not mounted.");
            return;
        }

        // On Android 11+, we need to ensure we have MANAGE_EXTERNAL_STORAGE to write here,
        // but checking Environment.isExternalStorageManager() should be done by the caller.
        
        SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
        Map<String, ?> allEntries = prefs.getAll();
        
        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON object", e);
            return;
        }

        File exportDir = new File(Environment.getExternalStorageDirectory(), EXPORT_DIR);
        if (!exportDir.exists()) {
            if (!exportDir.mkdirs()) {
                Log.e(TAG, "Failed to create export directory: " + exportDir.getAbsolutePath());
                return;
            }
        }

        File exportFile = new File(exportDir, EXPORT_FILE_NAME);
        try (FileWriter writer = new FileWriter(exportFile)) {
            writer.write(jsonObject.toString(4)); // 4 spaces for indentation
            Log.i(TAG, "Settings successfully exported to " + exportFile.getAbsolutePath());
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error writing JSON to file", e);
        }
    }

    public static void importSettingsFromJson(Context context) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File importFile = new File(Environment.getExternalStorageDirectory(), EXPORT_DIR + "/" + EXPORT_FILE_NAME);
        if (!importFile.exists()) {
            return; // Nothing to load
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(importFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading JSON file", e);
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            SharedPreferences prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = prefs.edit();

            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);
                if (value instanceof String) {
                    editor.putString(key, (String) value);
                } else if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                } else if (value instanceof Double) {
                    editor.putFloat(key, ((Double) value).floatValue());
                } else if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                }
            }
            editor.apply();
            Log.i(TAG, "Settings successfully imported from " + importFile.getAbsolutePath());
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
    }
}
