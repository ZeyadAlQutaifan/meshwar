package com.meshwar.meshwar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentSnapshot;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.models.Favorite;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.FireStore;
import com.meshwar.meshwar.util.Global;

public class FavoritePlaceRecyclerAdapter extends FirestoreRecyclerAdapter<Favorite , FavoritePlaceRecyclerAdapter.ViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *

     */

    public interface OnItemClickListener {
        void onItemClick(String placeId);
    }

    private OnItemClickListener onItemClickListener;
    public FavoritePlaceRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Favorite> options , OnItemClickListener onItemClickListener) {
        super(options);
        this.onItemClickListener = onItemClickListener ;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Favorite model) {
        String placeId = model.getPlaceId();
        FireStore.getPLace(placeId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult() ;
                    if (documentSnapshot.exists()){
                        Place place = documentSnapshot.toObject(Place.class);
                        holder.tvTitle.setText(place.getTitle());
                        holder.tvContent.setText(place.getDescription());
                        holder.chipCity.setText(place.getCity());
                        holder.views.setText(String.valueOf(place.getViews()));
                        if (place.getImages().size() > 0) {
                            Glide.with(holder.itemView.getContext())
                                    .load(Global.getPlaceImageNotFound(place.getImages().get(0)))
                                    .centerCrop()
                                    .into(holder.imgPlace);
                        }
                    }
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite , parent , false) ;
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;

        ImageView imgPlace;
        TextView tvTitle;
        TextView tvContent;
        Chip chipCity;
        Chip views;
        Chip distance;
        Chip weather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace = itemView.findViewById(R.id.imgPlace);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
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
                        onItemClickListener.onItemClick(getSnapshots().getSnapshot(getAdapterPosition()).getString("placeId"));
                    }
                }
            });
        }
    }
}
