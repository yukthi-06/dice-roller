package com.vypeensoft.diceroller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.vypeensoft.diceroller.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        
        String buildInfo = "App Version: " + com.vypeensoft.diceroller.BuildConfig.VERSION_NAME + "\n"
                + "Version Code: " + com.vypeensoft.diceroller.BuildConfig.VERSION_CODE + "\n"
                + "Build Type: " + com.vypeensoft.diceroller.BuildConfig.BUILD_TYPE + "\n"
                + "Application ID: " + com.vypeensoft.diceroller.BuildConfig.APPLICATION_ID + "\n"
                + "Git Version: " + com.vypeensoft.diceroller.BuildConfig.GIT_VERSION + "\n"
                + "Build Time: " + com.vypeensoft.diceroller.BuildConfig.BUILD_TIME;
        binding.tvBuildInfo.setText(buildInfo);
        
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
