package com.meshwar.meshwar.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.meshwar.meshwar.R;

import java.util.ArrayList;

public class RecyclerImageFromGalleryAdapter extends RecyclerView.Adapter<RecyclerImageFromGalleryAdapter.ViewHolder> {

    private ArrayList<Uri> uriArrayList;

    public RecyclerImageFromGalleryAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public RecyclerImageFromGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View imageView = layoutInflater.inflate(R.layout.item_image_create_new_post, parent, false);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerImageFromGalleryAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(uriArrayList.get(position))
                .into(holder.imgSelected);
        holder.imgDelete.setOnClickListener(v -> {
            uriArrayList.remove(uriArrayList.get(position));
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());

        });
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgSelected;
        ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSelected = itemView.findViewById(R.id.imgSelected);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
