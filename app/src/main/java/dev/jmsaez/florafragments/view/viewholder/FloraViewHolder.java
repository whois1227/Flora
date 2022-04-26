package dev.jmsaez.florafragments.view.viewholder;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import dev.jmsaez.florafragments.R;
import dev.jmsaez.florafragments.model.entity.Flora;


public class FloraViewHolder extends RecyclerView.ViewHolder {

    public Flora flora;
    public ImageView ivFlora;
    public TextView tvFlora;
    public MaterialCardView card;

    public FloraViewHolder(@NonNull View itemView) {
        super(itemView);
        ivFlora = itemView.findViewById(R.id.ivFlora);
        tvFlora = itemView.findViewById(R.id.lyNameFlora);
        card = itemView.findViewById(R.id.floraCard);

        card.setOnClickListener(l -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable("flora", flora);
            Navigation.findNavController(itemView).navigate(R.id.SecondFragment, bundle);
        });


    }

    public final ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
        return (ItemDetailsLookup.ItemDetails<Long>) (new ItemDetailsLookup.ItemDetails<Long>() {
            public int getPosition() {
                return getAdapterPosition();
            }

            public Long getSelectionKey() {
                return getItemId();
            }

        });
    }
}