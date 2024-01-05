package com.meshwar.meshwar.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.adapters.AllPlacesRecyclerAdapter;
import com.meshwar.meshwar.adapters.FavoritePlaceRecyclerAdapter;
import com.meshwar.meshwar.databinding.FragmentFavorateBinding;
import com.meshwar.meshwar.models.Favorite;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStore;


public class FavorateFragment extends Fragment {

    FragmentFavorateBinding binding;
    FavoritePlaceRecyclerAdapter favoritePlacesRecyclerAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavorateBinding.inflate(inflater, container, false);
        fillRecycler() ;
        return binding.getRoot();
    }

    private void fillRecycler() {
        FavoritePlaceRecyclerAdapter.OnItemClickListener itemClickListener = new FavoritePlaceRecyclerAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String placeId) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_view_tag,  ViewPlaceFragment.newInstance(placeId));
                transaction.addToBackStack(null);
                transaction.commit();
            }
        };
        Query query = FireStore.favRef().whereEqualTo("userId" , FireAuth.authInstance.getUid());
        FirestoreRecyclerOptions<Favorite> favOptions = new FirestoreRecyclerOptions.Builder<Favorite>()
                .setQuery(query, Favorite.class)
                .build();
        favoritePlacesRecyclerAdapter = new FavoritePlaceRecyclerAdapter(favOptions , itemClickListener) ;
        binding.recyclerFavPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFavPlaces.setAdapter(favoritePlacesRecyclerAdapter);
        favoritePlacesRecyclerAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        favoritePlacesRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        favoritePlacesRecyclerAdapter.stopListening();
    }
}