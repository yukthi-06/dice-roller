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
import com.vypeensoft.diceroller.adapters.AlphabetAdapter;
import com.vypeensoft.diceroller.databinding.FragmentAlphabetBinding;
import com.vypeensoft.diceroller.utils.PreferenceManager;
import com.vypeensoft.diceroller.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class AlphabetFragment extends Fragment {

    private FragmentAlphabetBinding binding;
    private PreferenceManager prefManager;
    private AlphabetAdapter adapter;
    private List<Character> alphabets;
    private int alphabetsCount;
    private Handler animationHandler;
    private Runnable animationRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAlphabetBinding.inflate(inflater, container, false);
        prefManager = new PreferenceManager(requireContext());
        alphabetsCount = 1; // Defaulting to 1 for simplicity, identical to number logic
        
        alphabets = new ArrayList<>();
        initAlphabets();

        adapter = new AlphabetAdapter(alphabets);
        binding.recyclerView.setAdapter(adapter);
        
        updateLayoutManager();
        updateCountUI();

        binding.btnMinus.setOnClickListener(v -> {
            if (alphabetsCount > 1) {
                alphabetsCount--;
                updateCountUI();
                initAlphabets();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            if (alphabetsCount < 10) {
                alphabetsCount++;
                updateCountUI();
                initAlphabets();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnAction.setOnClickListener(v -> generateAlphabets());

        return binding.getRoot();
    }

    private void initAlphabets() {
        alphabets.clear();
        for (int i = 0; i < alphabetsCount; i++) {
            alphabets.add('A');
        }
    }

    private void updateLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int total = alphabetsCount;
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
        binding.tvCount.setText(String.valueOf(alphabetsCount));
    }

    private void setControlsEnabled(boolean enabled) {
        binding.btnMinus.setEnabled(enabled);
        binding.btnPlus.setEnabled(enabled);
        binding.btnAction.setEnabled(enabled);
    }

    private void generateAlphabets() {
        setControlsEnabled(false);
        int duration = prefManager.getAnimationDuration();
        animationHandler = new Handler(Looper.getMainLooper());
        
        final long startTime = System.currentTimeMillis();
        
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < duration) {
                    for (int i = 0; i < alphabets.size(); i++) {
                        alphabets.set(i, RandomUtils.getRandomAlphabet());
                    }
                    adapter.notifyDataSetChanged();
                    animationHandler.postDelayed(this, 100);
                } else {
                    for (int i = 0; i < alphabets.size(); i++) {
                        alphabets.set(i, RandomUtils.getRandomAlphabet());
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
