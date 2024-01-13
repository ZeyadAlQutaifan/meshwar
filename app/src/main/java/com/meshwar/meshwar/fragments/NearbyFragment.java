package com.meshwar.meshwar.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meshwar.meshwar.NearbyMapActivity;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.databinding.FragmentNearbyBinding;


public class NearbyFragment extends Fragment {
    FragmentNearbyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNearbyBinding.inflate(inflater, container, false);
        binding.mosque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , NearbyMapActivity.class);
                intent.putExtra("KEY" ,"mosque");
                startActivity(intent);
            }
        });
        binding.atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , NearbyMapActivity.class);
                intent.putExtra("KEY" ,"atm");
                startActivity(intent);
            }
        });
        binding.gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , NearbyMapActivity.class);
                intent.putExtra("KEY" ,"gym");
                startActivity(intent);
            }
        });
        binding.hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , NearbyMapActivity.class);
                intent.putExtra("KEY" ,"lodging");
                startActivity(intent);
            }
        });
        binding.park.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , NearbyMapActivity.class);
                intent.putExtra("KEY" ,"park");
                startActivity(intent);
            }
        });
        binding.university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , NearbyMapActivity.class);
                intent.putExtra("KEY" ,"university");
                startActivity(intent);
            }
        });



        return binding.getRoot();
    }
}