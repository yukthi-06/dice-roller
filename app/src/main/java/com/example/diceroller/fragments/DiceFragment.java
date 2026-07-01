package com.example.diceroller.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.diceroller.adapters.DiceAdapter;
import com.example.diceroller.databinding.FragmentDiceBinding;
import com.example.diceroller.utils.PreferenceManager;
import com.example.diceroller.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class DiceFragment extends Fragment {

    private FragmentDiceBinding binding;
    private PreferenceManager prefManager;
    private DiceAdapter adapter;
    private List<Integer> diceFaces;
    private int diceCount;
    private Handler animationHandler;
    private Runnable animationRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDiceBinding.inflate(inflater, container, false);
        prefManager = new PreferenceManager(requireContext());
        diceCount = prefManager.getDefaultDiceCount();
        
        diceFaces = new ArrayList<>();
        initDiceFaces();

        adapter = new DiceAdapter(diceFaces);
        binding.recyclerView.setAdapter(adapter);
        
        updateCountUI();

        binding.btnMinus.setOnClickListener(v -> {
            if (diceCount > 1) {
                diceCount--;
                updateCountUI();
                initDiceFaces();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            if (diceCount < 10) {
                diceCount++;
                updateCountUI();
                initDiceFaces();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnAction.setOnClickListener(v -> rollDice());

        return binding.getRoot();
    }

    private void initDiceFaces() {
        diceFaces.clear();
        for (int i = 0; i < diceCount; i++) {
            diceFaces.add(1);
        }
    }

    private void updateCountUI() {
        binding.tvCount.setText(String.valueOf(diceCount));
    }

    private void setControlsEnabled(boolean enabled) {
        binding.btnMinus.setEnabled(enabled);
        binding.btnPlus.setEnabled(enabled);
        binding.btnAction.setEnabled(enabled);
    }

    private void rollDice() {
        setControlsEnabled(false);
        int duration = prefManager.getAnimationDuration();
        animationHandler = new Handler(Looper.getMainLooper());
        
        final long startTime = System.currentTimeMillis();
        
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < duration) {
                    // Randomize faces for animation
                    for (int i = 0; i < diceFaces.size(); i++) {
                        diceFaces.set(i, RandomUtils.getRandomDiceFace());
                    }
                    adapter.notifyDataSetChanged();
                    animationHandler.postDelayed(this, 100);
                } else {
                    // Final result
                    for (int i = 0; i < diceFaces.size(); i++) {
                        diceFaces.set(i, RandomUtils.getRandomDiceFace());
                    }
                    adapter.notifyDataSetChanged();
                    setControlsEnabled(true);
                }
            }
        };
        
        animationHandler.post(animationRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (animationHandler != null && animationRunnable != null) {
            animationHandler.removeCallbacks(animationRunnable);
        }
        binding = null;
    }
}
