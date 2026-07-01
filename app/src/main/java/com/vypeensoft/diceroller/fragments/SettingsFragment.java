package com.vypeensoft.diceroller.fragments;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.vypeensoft.diceroller.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
