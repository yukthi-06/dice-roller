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
        
        updateLayoutManager();
        updateCountUI();

        binding.btnMinus.setOnClickListener(v -> {
            if (diceCount > 1) {
                diceCount--;
                updateCountUI();
                initDiceFaces();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            if (diceCount < 10) {
                diceCount++;
                updateCountUI();
                initDiceFaces();
                updateLayoutManager();
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

    private void updateLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int total = diceCount;
                if (total == 1) return 6;
                if (total == 2) return 3;
                if (total == 3) return 2;
                if (total == 4) return 3; // 2 rows of 2
                if (total == 5) {
                    if (position < 3) return 2;
                    else return 3;
                }
                if (total == 6) return 2; // 2 rows of 3
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
        binding.tvCount.setText(String.valueOf(diceCount));
    }

    private void setControlsEnabled(boolean enabled) {
        binding.btnMinus.setEnabled(enabled);
        binding.btnPlus.setEnabled(enabled);
        binding.btnAction.setEnabled(enabled);
    }

    private int getDiceDrawable(int face) {
        switch (face) {
            case 1: return com.example.diceroller.R.drawable.dice_1;
            case 2: return com.example.diceroller.R.drawable.dice_2;
            case 3: return com.example.diceroller.R.drawable.dice_3;
            case 4: return com.example.diceroller.R.drawable.dice_4;
            case 5: return com.example.diceroller.R.drawable.dice_5;
            case 6: return com.example.diceroller.R.drawable.dice_6;
            default: return com.example.diceroller.R.drawable.dice_1;
        }
    }

    private void rollDice() {
        setControlsEnabled(false);
        int duration = prefManager.getAnimationDuration();
        animationHandler = new Handler(Looper.getMainLooper());
        
        final long startTime = System.currentTimeMillis();
        
        // Start physical view animations
        for (int i = 0; i < adapter.getItemCount(); i++) {
            androidx.recyclerview.widget.RecyclerView.ViewHolder holder = binding.recyclerView.findViewHolderForAdapterPosition(i);
            if (holder instanceof com.example.diceroller.adapters.DiceAdapter.DiceViewHolder) {
                View diceView = ((com.example.diceroller.adapters.DiceAdapter.DiceViewHolder) holder).binding.ivDice;
                diceView.animate()
                        .rotationBy((float) (360 * 4 * (Math.random() > 0.5 ? 1 : -1)))
                        .scaleX(1.3f)
                        .scaleY(1.3f)
                        .setDuration(duration / 2)
                        .setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator())
                        .withEndAction(() -> {
                            diceView.animate()
                                    .scaleX(1.0f)
                                    .scaleY(1.0f)
                                    .setDuration(duration / 2)
                                    .setInterpolator(new android.view.animation.BounceInterpolator())
                                    .start();
                        })
                        .start();
            }
        }
        
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < duration) {
                    // Randomize faces visually without breaking animation
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        androidx.recyclerview.widget.RecyclerView.ViewHolder holder = binding.recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder instanceof com.example.diceroller.adapters.DiceAdapter.DiceViewHolder) {
                            android.widget.ImageView iv = ((com.example.diceroller.adapters.DiceAdapter.DiceViewHolder) holder).binding.ivDice;
                            iv.setImageResource(getDiceDrawable(RandomUtils.getRandomDiceFace()));
                        }
                    }
                    animationHandler.postDelayed(this, 80);
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
