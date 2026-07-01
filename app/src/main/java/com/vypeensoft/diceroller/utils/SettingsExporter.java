package com.vypeensoft.diceroller.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
}
