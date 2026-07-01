package com.example.diceroller.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.diceroller.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAboutBinding.inflate(inflater, container, false);
        
        String buildInfo = "App Version: " + com.example.diceroller.BuildConfig.VERSION_NAME + "\n"
                + "Version Code: " + com.example.diceroller.BuildConfig.VERSION_CODE + "\n"
                + "Build Type: " + com.example.diceroller.BuildConfig.BUILD_TYPE + "\n"
                + "Application ID: " + com.example.diceroller.BuildConfig.APPLICATION_ID;
        binding.tvBuildInfo.setText(buildInfo);
        
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
