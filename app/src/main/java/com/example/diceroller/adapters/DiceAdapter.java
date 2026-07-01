package com.example.diceroller.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.diceroller.R;
import com.example.diceroller.databinding.ItemDiceBinding;
import java.util.List;

public class DiceAdapter extends RecyclerView.Adapter<DiceAdapter.DiceViewHolder> {

    private final List<Integer> diceFaces;

    public DiceAdapter(List<Integer> diceFaces) {
        this.diceFaces = diceFaces;
    }

    @NonNull
    @Override
    public DiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDiceBinding binding = ItemDiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DiceViewHolder holder, int position) {
        int face = diceFaces.get(position);
        int drawableRes;
        switch (face) {
            case 2: drawableRes = R.drawable.dice_2; break;
            case 3: drawableRes = R.drawable.dice_3; break;
            case 4: drawableRes = R.drawable.dice_4; break;
            case 5: drawableRes = R.drawable.dice_5; break;
            case 6: drawableRes = R.drawable.dice_6; break;
            case 1:
            default: drawableRes = R.drawable.dice_1; break;
        }
        holder.binding.ivDice.setImageResource(drawableRes);
    }

    @Override
    public int getItemCount() {
        return diceFaces.size();
    }

    public static class DiceViewHolder extends RecyclerView.ViewHolder {
        final ItemDiceBinding binding;
        public DiceViewHolder(ItemDiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
