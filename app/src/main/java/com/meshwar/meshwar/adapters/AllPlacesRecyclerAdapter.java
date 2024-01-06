package com.meshwar.meshwar.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.QuerySnapshot;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.Constant;
import com.meshwar.meshwar.util.FireStore;
import com.meshwar.meshwar.util.Global;

import org.json.JSONException;
import org.json.JSONObject;

public class AllPlacesRecyclerAdapter extends FirestoreRecyclerAdapter<Place, AllPlacesRecyclerAdapter.PlacesViewHolder> {
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(String placeId);
    }

    private OnItemClickListener onItemClickListener;

    public AllPlacesRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Place> options, Context mContext, OnItemClickListener onItemClickListener) {
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
        holder.favorites.setText("(" + model.getViews() + ") views");
        double distance = Global.calculateDistance(model.getLat() , model.getLng() , Constant.LATITUDE , Constant.LONGITUDE);
        String formatDistance = Global.formatDistance(distance);
        holder.distance.setText("(" + formatDistance + ") KM");
        if (model.getImages().size() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(Global.getPlaceImageNotFound(model.getImages().get(0)))
                    .centerCrop()
                    .into(holder.imgPlace);
        }
        FireStore.favRef().whereEqualTo("placeId" , getSnapshots().getSnapshot(position).getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    holder.favorites.setText(String.valueOf(queryDocumentSnapshots.size()));
                }
            }
        });

        String weatherURL = Constant.WEATHER_URI +
                Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(model.getLat())) +
                Constant.AND +
                Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(model.getLng())) +
                Constant.AND +
                Constant.KEY_BLOCK.replace("{#}", Constant.WEATHER_KEY);

        RequestQueue queue = Volley.newRequestQueue(holder.itemView.getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main = response.getJSONObject("main");
                    String temp = main.getString("temp");
                    double C = Double.parseDouble(temp) - 273.15;
                    holder.weather.setText(String.format("%.2f", C) + " C");

                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, error -> {
            // below line is use to display a toast message along with our error.

        });
        // at last we are adding our json
        // object request to our request
        // queue to fetch all the json data.
        queue.add(jsonObjectRequest);


    }

    private void showWeatherStatus(double longitude, double latitude, TextView textView) {

    }

    @NonNull
    @Override
    public PlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_places, parent, false);
        return new PlacesViewHolder(view);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;

        AppCompatImageView imgPlace;
        TextView tvTitle;
        TextView tvContent;
        TextView rates;
        RatingBar rate;
        Chip chipCity;
        Chip favorites;
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
            favorites = itemView.findViewById(R.id.views);
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
