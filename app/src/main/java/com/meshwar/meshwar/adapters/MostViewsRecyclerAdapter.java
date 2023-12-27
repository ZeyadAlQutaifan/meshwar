package com.meshwar.meshwar.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.databinding.ItemSuggestedPlaceBinding;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.Constant;
import com.meshwar.meshwar.util.Global;

public class MostViewsRecyclerAdapter  extends FirestoreRecyclerAdapter<Place, MostViewsRecyclerAdapter.MostViewHolder> {

    public MostViewsRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Place> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MostViewHolder holder, int position, @NonNull Place model) {
        holder.txtTitle.setText(Global.getNullString(model.getTitle()).length() > 22 ? Global.getNullString(model.getTitle()).substring(0, 20) + ".." : Global.getNullString(model.getTitle()));
        holder.txtCity.setText(model.getCity());
        Glide.with(holder.itemView.getContext())
                .load(Global.getPlaceImageNotFound(model.getImages().get(0)))
                .centerCrop()
                .into(holder.imageView);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final int i = holder.getAdapterPosition();
//                Intent intent = new Intent(holder.itemView.getContext(), PlaceActivity.class);
//                intent.putExtra(Constant.PASSING_OBJECT_KEY, model);
//                intent.putExtra(Constant.PASSING_REF_KEY, getSnapshots().getSnapshot(i).getKey());
//
//                startActivity(intent);

            }
        });
    }

    @NonNull
    @Override
    public MostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggested_place, parent, false);
        return new MostViewHolder(view);
    }

    public static class MostViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle;
        TextView txtCity;

        ImageView imageView;

        MaterialCardView container;

        public MostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.tvTitle);
            imageView = itemView.findViewById(R.id.imgPlace);
            container = itemView.findViewById(R.id.card);
            txtCity = itemView.findViewById(R.id.tvCity);
        }
    }
}
