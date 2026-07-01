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
import androidx.recyclerview.widget.RecyclerView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.widget.TextView;
import com.vypeensoft.diceroller.adapters.CardAdapter;
import com.vypeensoft.diceroller.databinding.FragmentCardsBinding;
import com.vypeensoft.diceroller.models.Card;
import com.vypeensoft.diceroller.utils.AnimationUtils;
import com.vypeensoft.diceroller.utils.PreferenceManager;
import com.vypeensoft.diceroller.utils.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class CardsFragment extends Fragment {

    private FragmentCardsBinding binding;
    private PreferenceManager prefManager;
    private CardAdapter adapter;
    private List<Card> cards;
    private int cardsCount;
    private Handler animationHandler;
    private Runnable animationRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCardsBinding.inflate(inflater, container, false);
        prefManager = new PreferenceManager(requireContext());
        cardsCount = prefManager.getDefaultCardsCount();
        
        cards = new ArrayList<>();
        initCards();

        adapter = new CardAdapter(cards);
        binding.recyclerView.setAdapter(adapter);
        
        updateLayoutManager();
        updateCountUI();

        binding.btnMinus.setOnClickListener(v -> {
            if (cardsCount > 1) {
                cardsCount--;
                updateCountUI();
                initCards();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnPlus.setOnClickListener(v -> {
            if (cardsCount < 10) {
                cardsCount++;
                updateCountUI();
                initCards();
                updateLayoutManager();
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnAction.setOnClickListener(v -> pickCards());

        return binding.getRoot();
    }

    private void initCards() {
        cards.clear();
        for (int i = 0; i < cardsCount; i++) {
            Card c = new Card("♠", "A", false);
            c.setFaceUp(false);
            cards.add(c);
        }
    }

    private void updateLayoutManager() {
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 6);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int total = cardsCount;
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
        binding.tvCount.setText(String.valueOf(cardsCount));
    }

    private void setControlsEnabled(boolean enabled) {
        binding.btnMinus.setEnabled(enabled);
        binding.btnPlus.setEnabled(enabled);
        binding.btnAction.setEnabled(enabled);
    }

    private void pickCards() {
        setControlsEnabled(false);
        
        // Reset all cards to face down instantly
        for (Card c : cards) {
            c.setFaceUp(false);
        }
        adapter.notifyDataSetChanged();
        
        // Wait for views to layout after reset
        binding.recyclerView.post(() -> {
            List<Card> finalCards = RandomUtils.pickRandomCards(cardsCount);
            for (Card c : finalCards) c.setFaceUp(true);
            animateCardSequentially(0, finalCards);
        });
    }

    private void animateCardSequentially(int index, List<Card> finalCards) {
        if (index >= cards.size()) {
            setControlsEnabled(true);
            return;
        }

        RecyclerView.ViewHolder holder = binding.recyclerView.findViewHolderForAdapterPosition(index);
        if (holder instanceof CardAdapter.CardViewHolder) {
            View cardView = ((CardAdapter.CardViewHolder) holder).binding.tvCard;
            
            // Fast spin animation before revealing
            ObjectAnimator fastSpin = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 1080f); // 3 full spins
            fastSpin.setDuration(600);
            fastSpin.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
            
            fastSpin.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cardView.setRotationY(0f);
                    ObjectAnimator flipOut = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 90f);
                    flipOut.setDuration(150);
                    flipOut.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Card finalCard = finalCards.get(index);
                            cards.set(index, finalCard);
                            
                            ((TextView) cardView).setBackgroundResource(com.vypeensoft.diceroller.R.drawable.card_bg);
                            ((TextView) cardView).setText(finalCard.getDisplayString());
                            ((TextView) cardView).setTextColor(finalCard.isRed() ? Color.RED : Color.BLACK);

                            cardView.setRotationY(-90f);
                            ObjectAnimator flipIn = ObjectAnimator.ofFloat(cardView, "rotationY", -90f, 0f);
                            flipIn.setDuration(150);
                            flipIn.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    animateCardSequentially(index + 1, finalCards);
                                }
                            });
                            flipIn.start();
                        }
                    });
                    flipOut.start();
                }
            });
            fastSpin.start();
        } else {
            // View might be null if scrolled off screen
            cards.set(index, finalCards.get(index));
            adapter.notifyItemChanged(index);
            animateCardSequentially(index + 1, finalCards);
        }
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
