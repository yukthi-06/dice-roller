package com.vypeensoft.diceroller.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vypeensoft.diceroller.R;
import com.vypeensoft.diceroller.databinding.ItemCardBinding;
import com.vypeensoft.diceroller.models.Card;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private final List<Card> cards;

    public CardAdapter(List<Card> cards) {
        this.cards = cards;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding binding = ItemCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cards.get(position);
        if (card.isFaceUp()) {
            holder.binding.tvCard.setBackgroundResource(R.drawable.card_bg);
            holder.binding.tvCard.setText(card.getDisplayString());
            holder.binding.tvCard.setTextColor(card.isRed() ? Color.RED : Color.BLACK);
        } else {
            holder.binding.tvCard.setBackgroundResource(R.drawable.card_back_bg);
            holder.binding.tvCard.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public final ItemCardBinding binding;
        public CardViewHolder(ItemCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
