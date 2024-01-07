package com.meshwar.meshwar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.models.Favorite;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.Constant;
import com.meshwar.meshwar.util.FireStore;
import com.meshwar.meshwar.util.Global;

import org.json.JSONException;
import org.json.JSONObject;

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
                        FireStore.favRef().whereEqualTo("placeId" , placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                                    holder.favorites.setText(String.valueOf(queryDocumentSnapshots.size()));
                                }
                            }
                        });

                        if (place.getImages().size() > 0) {
                            Glide.with(holder.itemView.getContext())
                                    .load(Global.getPlaceImageNotFound(place.getImages().get(0)))
                                    .centerCrop()
                                    .into(holder.imgPlace);
                            double distance = Global.calculateDistance(place.getLat() , place.getLng() , Constant.LATITUDE , Constant.LONGITUDE);
                            String formatDistance = Global.formatDistance(distance);
                            holder.distance.setText("(" + formatDistance + ") KM");
                            String weatherURL = Constant.WEATHER_URI +
                                    Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(place.getLat())) +
                                    Constant.AND +
                                    Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(place.getLng())) +
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
        Chip favorites;
        Chip distance;
        Chip weather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlace = itemView.findViewById(R.id.imgPlace);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            chipCity = itemView.findViewById(R.id.chipCity);
            favorites = itemView.findViewById(R.id.favorites);
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
