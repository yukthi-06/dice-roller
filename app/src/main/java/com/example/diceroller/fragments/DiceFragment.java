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
import androidx.recyclerview.widget.RecyclerView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.widget.ImageView;
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
        
        // Generate final result instantly
        List<Integer> finalFaces = new ArrayList<>();
        for (int i = 0; i < diceCount; i++) {
            finalFaces.add(RandomUtils.getRandomDiceFace());
        }
        
        // Start physical view animations
        for (int i = 0; i < adapter.getItemCount(); i++) {
            RecyclerView.ViewHolder holder = binding.recyclerView.findViewHolderForAdapterPosition(i);
            if (holder instanceof DiceAdapter.DiceViewHolder) {
                View diceView = ((DiceAdapter.DiceViewHolder) holder).binding.ivDice;
                
                // Continuous decelerating rotation over full duration
                ObjectAnimator rotate = ObjectAnimator.ofFloat(diceView, "rotation", diceView.getRotation(), diceView.getRotation() + (360 * 5 * (Math.random() > 0.5 ? 1 : -1)));
                rotate.setDuration(duration);
                rotate.setInterpolator(new android.view.animation.DecelerateInterpolator(1.5f));
                rotate.start();
                
                // Bounce scale animation
                ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(diceView, "scaleX", 1f, 1.3f);
                ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(diceView, "scaleY", 1f, 1.3f);
                scaleUpX.setDuration(duration / 2);
                scaleUpY.setDuration(duration / 2);
                scaleUpX.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
                scaleUpY.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
                
                scaleUpX.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(diceView, "scaleX", 1.3f, 1f);
                        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(diceView, "scaleY", 1.3f, 1f);
                        scaleDownX.setDuration(duration / 2);
                        scaleDownY.setDuration(duration / 2);
                        scaleDownX.setInterpolator(new android.view.animation.BounceInterpolator());
                        scaleDownY.setInterpolator(new android.view.animation.BounceInterpolator());
                        scaleDownX.start();
                        scaleDownY.start();
                    }
                });
                scaleUpX.start();
                scaleUpY.start();
            }
        }
        
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < duration / 2) { // Stop shuffling faces halfway through the rotation
                    // Randomize faces visually without breaking animation
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        RecyclerView.ViewHolder holder = binding.recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder instanceof DiceAdapter.DiceViewHolder) {
                            ImageView iv = ((DiceAdapter.DiceViewHolder) holder).binding.ivDice;
                            iv.setImageResource(getDiceDrawable(RandomUtils.getRandomDiceFace()));
                        }
                    }
                    animationHandler.postDelayed(this, 80);
                } else {
                    // Settle on final face visually while rotation finishes
                    for (int i = 0; i < adapter.getItemCount(); i++) {
                        RecyclerView.ViewHolder holder = binding.recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder instanceof DiceAdapter.DiceViewHolder) {
                            ImageView iv = ((DiceAdapter.DiceViewHolder) holder).binding.ivDice;
                            iv.setImageResource(getDiceDrawable(finalFaces.get(i)));
                        }
                    }
                    
                    // Update data model
                    for (int i = 0; i < diceFaces.size(); i++) {
                        diceFaces.set(i, finalFaces.get(i));
                    }
                    
                    // Wait for the remaining rotation to complete before enabling controls
                    long remainingTime = duration - elapsedTime;
                    if (remainingTime > 0) {
                        animationHandler.postDelayed(() -> {
                            adapter.notifyDataSetChanged();
                            setControlsEnabled(true);
                        }, remainingTime);
                    } else {
                        adapter.notifyDataSetChanged();
                        setControlsEnabled(true);
                    }
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
