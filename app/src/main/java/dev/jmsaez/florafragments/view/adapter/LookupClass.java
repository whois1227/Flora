package dev.jmsaez.florafragments.view.adapter;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

import dev.jmsaez.florafragments.view.viewholder.FloraViewHolder;

public class LookupClass extends ItemDetailsLookup<Long> {

    private RecyclerView rvFlora;

    public LookupClass(RecyclerView recyclerView){
        this.rvFlora = recyclerView;
    }
    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = rvFlora.findChildViewUnder(e.getX(), e.getY());
        if(view != null){
            return ((FloraViewHolder) rvFlora.getChildViewHolder(view)).getItemDetails();
        }
        return null;
    }
}
