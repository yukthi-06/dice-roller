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
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.diceroller.adapters.WordAdapter;
import com.example.diceroller.databinding.FragmentWordsBinding;
import com.example.diceroller.utils.PreferenceManager;
import com.example.diceroller.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class WordsFragment extends Fragment {

    private FragmentWordsBinding binding;
    private PreferenceManager prefManager;
    private WordAdapter adapter;
    private List<String> words;
    private int wordsCount;
    private Handler animationHandler;
    private Runnable animationRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWordsBinding.inflate(inflater, container, false);
        prefManager = new PreferenceManager(requireContext());
        wordsCount = 1; // Defaulting to 1 for simplicity, identical to alphabet logic
        
        RandomUtils.initDictionary(requireContext());
        
        words = new ArrayList<>();
        initWords();

        adapter = new WordAdapter(words);
        binding.recyclerView.setAdapter(adapter);
        
        updateLayoutManager();
        updateCountUI();

        binding.btnMinus.setOnClickListener(v -> {
            if (wordsCount > 1) {
                wordsCount--;
                updateCountUI();
                initWords();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            if (wordsCount < 10) {
                wordsCount++;
                updateCountUI();
                initWords();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnAction.setOnClickListener(v -> generateWords());

        return binding.getRoot();
    }

    private void initWords() {
        words.clear();
        for (int i = 0; i < wordsCount; i++) {
            words.add("Word");
        }
    }

    private void updateLayoutManager() {
        androidx.recyclerview.widget.LinearLayoutManager layoutManager = new androidx.recyclerview.widget.LinearLayoutManager(requireContext());
        binding.recyclerView.setLayoutManager(layoutManager);
    }

    private void updateCountUI() {
        binding.tvCount.setText(String.valueOf(wordsCount));
    }

    private void setControlsEnabled(boolean enabled) {
        binding.btnMinus.setEnabled(enabled);
        binding.btnPlus.setEnabled(enabled);
        binding.btnAction.setEnabled(enabled);
    }

    private void generateWords() {
        setControlsEnabled(false);
        int duration = prefManager.getAnimationDuration();
        animationHandler = new Handler(Looper.getMainLooper());
        
        final long startTime = System.currentTimeMillis();
        
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < duration) {
                    for (int i = 0; i < words.size(); i++) {
                        words.set(i, RandomUtils.getRandomGibberishWord());
                    }
                    adapter.notifyDataSetChanged();
                    animationHandler.postDelayed(this, 100);
                } else {
                    for (int i = 0; i < words.size(); i++) {
                        words.set(i, RandomUtils.getRandomWord());
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
