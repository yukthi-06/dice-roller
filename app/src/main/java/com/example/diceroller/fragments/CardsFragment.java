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
import com.example.diceroller.adapters.CardAdapter;
import com.example.diceroller.databinding.FragmentCardsBinding;
import com.example.diceroller.models.Card;
import com.example.diceroller.utils.AnimationUtils;
import com.example.diceroller.utils.PreferenceManager;
import com.example.diceroller.utils.RandomUtils;
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
        int duration = prefManager.getAnimationDuration();
        animationHandler = new Handler(Looper.getMainLooper());
        
        final long startTime = System.currentTimeMillis();
        
        // Animate the RecyclerView slightly for visual effect
        AnimationUtils.rotateView(binding.recyclerView, duration);
        
        animationRunnable = new Runnable() {
            @Override
            public void run() {
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime < duration) {
                    List<Card> randomDeck = RandomUtils.pickRandomCards(cardsCount);
                    for (int i = 0; i < cards.size(); i++) {
                        Card c = randomDeck.get(i);
                        // Randomly show front or back
                        c.setFaceUp(Math.random() > 0.5);
                        cards.set(i, c);
                    }
                    adapter.notifyDataSetChanged();
                    animationHandler.postDelayed(this, 200);
                } else {
                    List<Card> finalCards = RandomUtils.pickRandomCards(cardsCount);
                    for (int i = 0; i < cards.size(); i++) {
                        Card c = finalCards.get(i);
                        c.setFaceUp(true);
                        cards.set(i, c);
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
