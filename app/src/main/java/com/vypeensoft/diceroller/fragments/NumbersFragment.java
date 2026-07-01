package com.vypeensoft.diceroller.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.vypeensoft.diceroller.adapters.NumberAdapter;
import com.vypeensoft.diceroller.databinding.FragmentNumbersBinding;
import com.vypeensoft.diceroller.utils.PreferenceManager;
import com.vypeensoft.diceroller.utils.RandomUtils;
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
        
        updateLayoutManager();
        updateCountUI();

        binding.btnMinus.setOnClickListener(v -> {
            if (numbersCount > 1) {
                numbersCount--;
                updateCountUI();
                initNumbers();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            if (numbersCount < 10) {
                numbersCount++;
                updateCountUI();
                initNumbers();
                updateLayoutManager();
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

    private void updateLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int total = numbersCount;
                if (total == 1) return 6;
                if (total == 2) return 3;
                if (total == 3) return 2;
                if (total == 4) return 3;
                if (total == 5) {
                    if (position < 3) return 2;
                    else return 3;
                }
                if (total == 6) return 2;
                if (total == 7) {
                    if (position < 3) return 2;
                    else return 3;
                }
                if (total == 8) {
                    if (position < 6) return 2;
                    else return 3;
                }
                if (total == 9) return 2;
                if (total == 10) {
                    if (position < 9) return 2;
                    else return 6;
                }
                return 2;
            }
        });
        binding.recyclerView.setLayoutManager(layoutManager);
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
