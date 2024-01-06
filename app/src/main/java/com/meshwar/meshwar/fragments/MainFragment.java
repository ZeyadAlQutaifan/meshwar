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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.meshwar.meshwar.GPS.GPSTracker;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.adapters.LatestRecyclerAdapter;
import com.meshwar.meshwar.databinding.FragmentMainBinding;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;


public class MainFragment extends Fragment {

    FragmentMainBinding binding;
    double longitude = 0, latitude = 0;

    private LatestRecyclerAdapter latestRecyclerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);

        Query query = placesRef().orderBy("createdAt", Query.Direction.DESCENDING).limit(5);
        FirestoreRecyclerOptions<Place> placeOptions = new FirestoreRecyclerOptions.Builder<Place>()
                .setQuery(query, Place.class)
                .build();
        latestRecyclerAdapter = new LatestRecyclerAdapter(placeOptions);
        binding.suggestedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.suggestedRecycler.setAdapter(latestRecyclerAdapter);

        binding.txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllPlacesFragment fragment2 = new AllPlacesFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container_view_tag, fragment2);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        latestRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        latestRecyclerAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    private void getLocation() {
        GPSTracker tracker = new GPSTracker(getActivity());
        if (!tracker.canGetLocation()) {
            tracker.showSettingsAlert();
        } else {
            latitude = tracker.getLatitude();
            longitude = tracker.getLongitude();

            Constant.LATITUDE = latitude;
            Constant.LONGITUDE = longitude;
            showWeatherStatus();
        }
    }

    private void showWeatherStatus() {
        String weatherURL = Constant.WEATHER_URI +
                Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(latitude)) +
                Constant.AND +
                Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(longitude)) +
                Constant.AND +
                Constant.KEY_BLOCK.replace("{#}", Constant.WEATHER_KEY);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        // this is the error listener method which
// we will call if we get any error from API.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main = response.getJSONObject("main");
                    String temp = main.getString("temp");
                    String city = response.getString("name");
                    double C = Double.parseDouble(temp) - 273.15;
                    binding.txtTemp.setText(String.format("%.2f", C) + " C");
                    binding.txtCityName.setText(city);

                } catch (JSONException e) {
                    // if we do not extract data from json object properly.
                    // below line of code is use to handle json exception
                    e.printStackTrace();
                }
            }
        }, error -> {
            // below line is use to display a toast message along with our error.
            Toast.makeText(getActivity(), "Fail to get data..", Toast.LENGTH_SHORT).show();
        });
        // at last we are adding our json
        // object request to our request
        // queue to fetch all the json data.
        queue.add(jsonObjectRequest);

    }
}