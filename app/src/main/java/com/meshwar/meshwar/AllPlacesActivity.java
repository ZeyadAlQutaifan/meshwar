package com.meshwar.meshwar;

import static com.meshwar.meshwar.util.FireStore.placesRef;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.meshwar.meshwar.adapters.AllPlacesRecyclerAdapter;
import com.meshwar.meshwar.databinding.ActivityAllPlacesActivtityBinding;
import com.meshwar.meshwar.databinding.ActivitySignUpBinding;
import com.meshwar.meshwar.models.Place;

public class AllPlacesActivity extends AppCompatActivity {

    ActivityAllPlacesActivtityBinding binding;
    private AllPlacesRecyclerAdapter allPlacesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllPlacesActivtityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Query query = placesRef();
        FirestoreRecyclerOptions<Place> placeOptions = new FirestoreRecyclerOptions.Builder<Place>()
                .setQuery(query, Place.class)
                .build();
//        allPlacesRecyclerAdapter = new AllPlacesRecyclerAdapter(placeOptions, this);
//        binding.recyclerAllPlaces.setLayoutManager(new LinearLayoutManager(this));
//        binding.recyclerAllPlaces.setAdapter(allPlacesRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        allPlacesRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        allPlacesRecyclerAdapter.stopListening();
    }

}