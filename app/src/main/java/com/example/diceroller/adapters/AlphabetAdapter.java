package com.example.diceroller.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diceroller.databinding.ItemAlphabetBinding;
import java.util.List;

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.AlphabetViewHolder> {

    private final List<Character> alphabets;

    public AlphabetAdapter(List<Character> alphabets) {
        this.alphabets = alphabets;
    }

    @NonNull
    @Override
    public AlphabetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlphabetBinding binding = ItemAlphabetBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlphabetViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlphabetViewHolder holder, int position) {
        holder.binding.tvAlphabet.setText(String.valueOf(alphabets.get(position)));
    }

    @Override
    public int getItemCount() {
        return alphabets.size();
    }

    public static class AlphabetViewHolder extends RecyclerView.ViewHolder {
        final ItemAlphabetBinding binding;
        public AlphabetViewHolder(ItemAlphabetBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
