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
import com.example.diceroller.adapters.NumberAdapter;
import com.example.diceroller.databinding.FragmentNumbersBinding;
import com.example.diceroller.utils.PreferenceManager;
import com.example.diceroller.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class NumbersFragment extends Fragment {

    private FragmentNumbersBinding binding;
    private PreferenceManager prefManager;
    private NumberAdapter adapter;
    private List<Integer> numbers;
    private int numbersCount;
    private Handler animationHandler;
    private Runnable animationRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNumbersBinding.inflate(inflater, container, false);
        prefManager = new PreferenceManager(requireContext());
        numbersCount = prefManager.getDefaultNumbersCount();
        
        numbers = new ArrayList<>();
        initNumbers();

        adapter = new NumberAdapter(numbers);
        binding.recyclerView.setAdapter(adapter);
        
        updateCountUI();

        binding.btnMinus.setOnClickListener(v -> {
            if (numbersCount > 1) {
                numbersCount--;
                updateCountUI();
                initNumbers();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            if (numbersCount < 10) {
                numbersCount++;
                updateCountUI();
                initNumbers();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnAction.setOnClickListener(v -> generateNumbers());

        return binding.getRoot();
    }

    private void initNumbers() {
        numbers.clear();
        for (int i = 0; i < numbersCount; i++) {
            numbers.add(0);
        }
    }

    private void updateCountUI() {
        binding.tvCount.setText(String.valueOf(numbersCount));
    }

    private void setControlsEnabled(boolean enabled) {
        binding.btnMinus.setEnabled(enabled);
        binding.btnPlus.setEnabled(enabled);
        binding.btnAction.setEnabled(enabled);
    }

    private void generateNumbers() {
        setControlsEnabled(false);
        int duration = prefManager.getAnimationDuration();
        animationHandler = new Handler(Looper.getMainLooper());
        
        final long startTime = System.currentTimeMillis();
        
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < duration) {
                    for (int i = 0; i < numbers.size(); i++) {
                        numbers.set(i, RandomUtils.getRandomNumber(100));
                    }
                    adapter.notifyDataSetChanged();
                    animationHandler.postDelayed(this, 100);
                } else {
                    for (int i = 0; i < numbers.size(); i++) {
                        numbers.set(i, RandomUtils.getRandomNumber(100));
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
