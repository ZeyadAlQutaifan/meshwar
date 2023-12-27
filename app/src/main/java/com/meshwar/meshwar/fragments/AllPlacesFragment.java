package com.meshwar.meshwar.fragments;

import static com.meshwar.meshwar.util.FireStore.placesRef;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.adapters.AllPlacesRecyclerAdapter;
import com.meshwar.meshwar.databinding.FragmentAllPlacesBinding;
import com.meshwar.meshwar.models.Place;


public class AllPlacesFragment extends Fragment {

    FragmentAllPlacesBinding binding ;
    private AllPlacesRecyclerAdapter allPlacesRecyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentAllPlacesBinding.inflate(inflater, container, false);
        Query query = placesRef();
        FirestoreRecyclerOptions<Place> placeOptions = new FirestoreRecyclerOptions.Builder<Place>()
                .setQuery(query, Place.class)
                .build();
        AllPlacesRecyclerAdapter.OnItemClickListener itemClickListener = new AllPlacesRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String placeId) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_view_tag,  ViewPlaceFragment.newInstance(placeId));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };

        allPlacesRecyclerAdapter = new AllPlacesRecyclerAdapter(placeOptions, getContext() , itemClickListener);
        binding.recyclerAllPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerAllPlaces.setAdapter(allPlacesRecyclerAdapter);

        return binding.getRoot()  ;
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