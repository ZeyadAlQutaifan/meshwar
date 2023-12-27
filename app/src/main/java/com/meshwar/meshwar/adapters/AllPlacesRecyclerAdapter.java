package com.meshwar.meshwar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.ViewPlaceActivity;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.Global;

public class AllPlacesRecyclerAdapter extends FirestoreRecyclerAdapter<Place, AllPlacesRecyclerAdapter.PlacesViewHolder> {
    private Context mContext  ;
    public interface OnItemClickListener {
        void onItemClick(String placeId);
    }

    private OnItemClickListener onItemClickListener;
    public AllPlacesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Place> options, Context mContext , OnItemClickListener onItemClickListener) {
        super(options);
        this.mContext = mContext;
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    protected void onBindViewHolder(@NonNull PlacesViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Place model) {
        holder.tvTitle.setText(Global.getNullString(model.getTitle()).length() > 22 ? Global.getNullString(model.getTitle()).substring(0, 20) + ".." : Global.getNullString(model.getTitle()));
        holder.tvContent.setText(model.getDescription());
        holder.rate.setRating((float) model.getRate());
        holder.rates.setText("(" + model.getRates() + ") " + model.getRate());
        holder.chipCity.setText(model.getCity());
        holder.views.setText("(" + model.getViews() + ") views");
        double distance = 3.4;
        holder.distance.setText("(" + distance + ") KM");
        double weather = 23.5;
        holder.weather.setText("(" + weather + ") C");
        Glide.with(holder.itemView.getContext())
                .load(Global.getPlaceImageNotFound(model.getImages().get(0)))
                .centerCrop()
                .into(holder.imgPlace);
    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_places, parent, false);
        return new PlacesViewHolder(view);
    }

    public  class PlacesViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;

        AppCompatImageView imgPlace;
        TextView tvTitle;
        TextView tvContent;
        TextView rates;
        RatingBar rate;
        Chip chipCity;
        Chip views;
        Chip distance;
        Chip weather;

        public PlacesViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace = itemView.findViewById(R.id.imgPlace);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            rates = itemView.findViewById(R.id.rates);
            rate = itemView.findViewById(R.id.rate);
            chipCity = itemView.findViewById(R.id.chipCity);
            views = itemView.findViewById(R.id.views);
            distance = itemView.findViewById(R.id.distance);
            weather = itemView.findViewById(R.id.weather);
            materialCardView = itemView.findViewById(R.id.materialCardView);
            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call onItemClick when an item is clicked
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getId());
                    }
                }
            });
        }
    }
}
