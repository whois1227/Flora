package dev.jmsaez.florafragments.view.adapter;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.jmsaez.florafragments.R;
import dev.jmsaez.florafragments.model.entity.Flora;
import dev.jmsaez.florafragments.view.viewholder.FloraViewHolder;
import dev.jmsaez.florafragments.viewmodel.MainActivityViewModel;

public class FloraAdapter extends RecyclerView.Adapter<FloraViewHolder> {

    ArrayList<Flora> floraList;
    Context context;
    private SelectionTracker<Long> tracker;
    ArrayList<Flora> florasToDelete = new ArrayList<>();
    MainActivityViewModel mavm;

    final String URL_IMG = "https://informatica.ieszaidinvergeles.org:10016/AD/felixRDLFapp/public/api/imagen/";

    public FloraAdapter(Context context, MainActivityViewModel mavm){
        setHasStableIds(true);
        this.context = context;
        this.mavm = mavm;
    }

    @NonNull
    @Override
    public FloraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flora_item, parent, false);
        return new FloraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FloraViewHolder holder, int position) {
        Flora flora = floraList.get(position);
        holder.flora = flora;

        Glide.with(context).load(URL_IMG + flora.getId()+"/flora").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(holder.ivFlora);
        holder.tvFlora.setText(flora.getNombre());

        if(tracker.isSelected((long) position)){
            florasToDelete.add(flora);
            holder.card.setChecked(true);
        } else {
            holder.card.setChecked(false);
            florasToDelete.remove(flora);
        }
    }



    @Override
    public int getItemCount() {
        if(floraList != null){
            return floraList.size();
        }
        return 0;
    }

    public void setListFlora(ArrayList<Flora> flora){
        this.floraList = flora;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setTracker(SelectionTracker<Long> tracker) {
        this.tracker = tracker;
    }

    public void delete(){
        for (int i = 0; i < florasToDelete.size(); i++) {
            mavm.deleteFlora(florasToDelete.get(i).getId());
        }
    }
}
