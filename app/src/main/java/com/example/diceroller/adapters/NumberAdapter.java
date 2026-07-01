package com.example.diceroller.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diceroller.databinding.ItemNumberBinding;
import java.util.List;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberViewHolder> {

    private final List<Integer> numbers;

    public NumberAdapter(List<Integer> numbers) {
        this.numbers = numbers;
    }

    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNumberBinding binding = ItemNumberBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NumberViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.binding.tvNumber.setText(String.valueOf(numbers.get(position)));
    }

    @Override
    public int getItemCount() {
        return numbers.size();
    }

    public static class NumberViewHolder extends RecyclerView.ViewHolder {
        final ItemNumberBinding binding;
        public NumberViewHolder(ItemNumberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
