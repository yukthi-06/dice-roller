package com.vypeensoft.diceroller.fragments;

import android.os.Bundle;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import com.vypeensoft.diceroller.R;
import com.vypeensoft.diceroller.utils.MemoryPreferenceDataStore;

public class SettingsFragment extends PreferenceFragmentCompat {

    private MemoryPreferenceDataStore memoryStore;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        memoryStore = new MemoryPreferenceDataStore(getPreferenceManager().getSharedPreferences());
        getPreferenceManager().setPreferenceDataStore(memoryStore);
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if ("action_save".equals(preference.getKey())) {
            memoryStore.saveToRealPrefs();
            Toast.makeText(getContext(), "Configuration Saved", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onPreferenceTreeClick(preference);
    }
}
