package com.meshwar.meshwar.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meshwar.meshwar.LoginActivity;
import com.meshwar.meshwar.MainActivity;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.adapters.CommentsRecyclerAdapter;
import com.meshwar.meshwar.databinding.FragmentMyPostBinding;
import com.meshwar.meshwar.databinding.FragmentViewPlaceBinding;
import com.meshwar.meshwar.models.Comment;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.Constant;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStore;
import com.meshwar.meshwar.util.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class ViewPlaceFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    List<SlideModel> imageList = new ArrayList<SlideModel>();

    FragmentViewPlaceBinding binding;
    private static final String ARG_PLACE_ID = "param1";
    private boolean isFavorite = false;

    private String placeId;
    private static final LatLng SYDNEY_LOCATION = new LatLng(-33.8688, 151.2093);

    public ViewPlaceFragment() {
        // Required empty public constructor
    }

    public static ViewPlaceFragment newInstance(String param1) {
        ViewPlaceFragment fragment = new ViewPlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PLACE_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            placeId = getArguments().getString(ARG_PLACE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewPlaceBinding.inflate(inflater, container, false);
        binding.mapView2.onCreate(savedInstanceState);
        binding.mapView2.getMapAsync(this);
        binding.btnLike.setCheckable(true);

        initCommentButton();
        initLikeButton();
        getPlaceData();
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Delete Post")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("Deleting...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                FireStore.removePlace(placeId).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                        progressDialog.dismiss();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        })
                        .setCancelable(false)
                        .show();


            }
        });
        return binding.getRoot();
    }

    /**
     * Retrieves place data from Firestore based on the provided placeId.
     * Populates the UI with the retrieved place information, including images, title, description,
     * city, favorite count, distance, weather, comment count, and checks if the place is marked as a favorite.
     */
    private void getPlaceData() {

        // Retrieve place data from Firestore
        FireStore.getPLace(placeId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // Check if the Firestore task was successful
                if (task.isSuccessful()) {
                    // Get the DocumentSnapshot containing place information
                    DocumentSnapshot documentSnapshot = task.getResult();

                    // Convert DocumentSnapshot to Place object
                    Place place = documentSnapshot.toObject(Place.class);
                    if (place.getWriterId().equals(FireAuth.authInstance.getCurrentUser().getUid())) {
                        binding.btnDelete.setVisibility(View.VISIBLE);
                        binding.btnUpdate.setVisibility(View.VISIBLE);
                    }

                    // Populate the image slider with place images
                    for (String uri : place.getImages()) {
                        imageList.add(new SlideModel(uri, null, ScaleTypes.CENTER_CROP));
                    }
                    binding.imageSlider.setImageList(imageList);

                    // Set place details in the UI
                    binding.txtTitle.setText(place.getTitle());
                    binding.txtDescription.setText(place.getDescription());
                    binding.chipCity.setText(place.getCity());

                    // Set favorite count in the UI
                    setFavoriteCount();

                    // Set distance between user and the place in the UI
                    setDistance(place.getLat(), place.getLng());

                    // Set weather information for the place in the UI
                    setWeather(place.getLat(), place.getLng());

                    // Get and set the comment count for the place
                    getPlaceCommentCount();

                    // Check if the place is marked as a favorite and update UI accordingly
                    findIfFavorite();

                    // Update the map to display the location of the place with a specific zoom level
                    updateLocation(new LatLng(place.getLat(), place.getLng()), 18);
                    binding.btnNavigate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place.getLat() + "," + place.getLng() + "&mode=d");
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                    });
                }
            }
        });
    }


    private void setWeather(double lat, double lng) {
        String weatherURL = Constant.WEATHER_URI +
                Constant.LATITUDE_BLOCK.replace("{#}", String.valueOf(lat)) +
                Constant.AND +
                Constant.LONGITUDE_BLOCK.replace("{#}", String.valueOf(lng)) +
                Constant.AND +
                Constant.KEY_BLOCK.replace("{#}", Constant.WEATHER_KEY);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, weatherURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject main = response.getJSONObject("main");
                    String temp = main.getString("temp");
                    double C = Double.parseDouble(temp) - 273.15;
                    binding.weather.setText(String.format("%.2f", C) + " C");

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

    /**
     * Checks if the current place is marked as a favorite by the user.
     * Updates the 'isFavorite' flag accordingly and sets the state of the 'Like' button in the UI.
     */
    private void findIfFavorite() {
        // Query Firestore to check if the current place is marked as a favorite
        FireStore.getIfFavorite(placeId)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // Check if the Firestore task was successful
                        if (task.isSuccessful()) {
                            // Get the QuerySnapshot containing documents related to favorites
                            QuerySnapshot queryDocumentSnapshots = task.getResult();

                            // Check if there are documents (indicating the place is a favorite)
                            if (queryDocumentSnapshots.getDocuments().size() > 0) {
                                // Set 'isFavorite' flag to true
                                isFavorite = true;
                                // Set the 'Like' button in the UI to the checked state
                                binding.btnLike.setChecked(true);
                            } else {
                                // Set 'isFavorite' flag to false
                                isFavorite = false;
                                // Set the 'Like' button in the UI to the unchecked state
                                binding.btnLike.setChecked(false);
                            }
                        } else {
                            // Handle the case where the Firestore task was not successful
                            // (e.g., network issues or permission problems)
                        }
                    }
                });
    }


    /**
     * Calculates the distance between the provided latitude and longitude coordinates
     * and a constant reference location (e.g., user's current location).
     * Displays the formatted distance in kilometers in the UI.
     *
     * @param latitude  Latitude of the place.
     * @param longitude Longitude of the place.
     */
    private void setDistance(double latitude, double longitude) {
        // Calculate the distance between the place and a constant reference location
        double distance = Global.calculateDistance(latitude, longitude, Constant.LATITUDE, Constant.LONGITUDE);

        // Format the distance to two decimal points
        String formatDistance = Global.formatDistance(distance);

        // Display the formatted distance in the UI
        binding.distance.setText("(" + formatDistance + ") KM");
    }

    /**
     * Retrieves the number of comments associated with the current place from Firestore.
     * Updates the comment count in the UI.
     */
    private void getPlaceCommentCount() {
        // Query Firestore to get comments related to the current place
        FireStore.getPlaceComments(placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // Check if the Firestore task was successful
                if (task.isSuccessful()) {
                    // Get the QuerySnapshot containing documents related to place comments
                    QuerySnapshot queryDocumentSnapshots = task.getResult();

                    // Update the comment count in the UI based on the number of documents
                    binding.txtCommentCount.setText(String.valueOf(queryDocumentSnapshots.size()));
                }
                // Note: If the task is not successful, the comment count in the UI remains unchanged
            }
        });
    }

    private void initCommentButton() {
        binding.txtCommentCount.setOnClickListener(v -> showDialog());

    }

    /**
     * Initializes the 'Like' button, handling its click events to toggle the favorite state.
     * Updates the Firestore database based on the user's interaction and provides feedback through toasts.
     */
    private void initLikeButton() {
        // Set a click listener for the 'Like' button using lambda expression
        binding.btnLike.setOnClickListener(v -> {
            // Toggle the favorite state
            isFavorite = !isFavorite;

            // Disable the 'Like' button temporarily to prevent rapid clicks
            binding.btnLike.setEnabled(false);

            // Update Firestore based on the user's interaction
            if (isFavorite) {
                // Add the current place to favorites
                FireStore.addPlaceToFav(placeId).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        // Display a toast indicating successful addition to favorites
                        Toast.makeText(getActivity(), "Added To Favorites", Toast.LENGTH_SHORT).show();
                        // Enable the 'Like' button after the operation is complete
                        binding.btnLike.setEnabled(true);
                        // Update the favorite count in the UI
                        setFavoriteCount();
                    }
                });
            } else {
                // Remove the current place from favorites
                FireStore.removePlaceFromFav(placeId).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Display a toast indicating successful removal from favorites
                        Toast.makeText(getActivity(), "Removed From Favorites", Toast.LENGTH_SHORT).show();
                        // Enable the 'Like' button after the operation is complete
                        binding.btnLike.setEnabled(true);
                        // Update the favorite count in the UI
                        setFavoriteCount();
                    }
                });
            }
        });
    }

    private void setFavoriteCount() {
        FireStore.favRef().whereEqualTo("placeId", placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    binding.favorites.setText(String.valueOf(queryDocumentSnapshots.size()));
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move the camera to Sydney and add a marker
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY_LOCATION, 10));
        mMap.addMarker(new MarkerOptions().position(SYDNEY_LOCATION).title("Sydney Marker"));

    }

    private void updateLocation(LatLng location, float zoomLevel) {
        if (mMap != null) {
            // Clear existing markers before adding a new one
            mMap.clear();

            // Move the camera to the specified location and set the zoom level
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));

            // Add a marker at the specified location
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title("Marker Title")  // You can customize the title
                    .snippet("Marker Snippet");  // You can customize the snippet
            mMap.addMarker(markerOptions);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.mapView2.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView2.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView2.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.mapView2.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView2.onLowMemory();
    }

    /**
     * Displays a bottom sheet dialog for adding and viewing comments related to the current place.
     * Users can input comments, send them, and view existing comments in a RecyclerView.
     */
    private void showDialog() {
        // Create a new dialog
        final Dialog dialog = new Dialog(getActivity());

        // Set dialog properties
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comments_bottom_sheet_layout);

        // Initialize UI components in the dialog
        TextInputEditText etComment = dialog.findViewById(R.id.etComment);
        Button btnSend = dialog.findViewById(R.id.btnSend);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        // Load existing comments into the RecyclerView
        loadComments(recyclerView);

        // Set click listener for the 'Send' button
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the comment body from the input field
                String body = etComment.getText().toString();

                // Create a Comment object with the input data
                Comment comment = new Comment();
                comment.setBody(body);
                comment.setPlaceId(placeId);
                comment.setCreationTime(System.currentTimeMillis());
                comment.setOwnerID(FireAuth.authInstance.getUid());

                // Add the comment to Firestore
                FireStore.addComment(comment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        // Clear the input field after sending the comment
                        etComment.setText("");
                        // Reload the comments in the RecyclerView
                        loadComments(recyclerView);
                    }
                });
            }
        });

        // Show the dialog
        dialog.show();

        // Set dialog window properties
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    /**
     * Loads comments related to the current place from Firestore and displays them in a RecyclerView.
     *
     * @param recyclerView The RecyclerView to display the comments.
     */
    private void loadComments(RecyclerView recyclerView) {
        // Query Firestore to get comments related to the current place
        Query query = FireStore.getPlaceComments(placeId);

        // Create options for the FirestoreRecyclerAdapter
        FirestoreRecyclerOptions<Comment> commentOptions = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();

        // Initialize and set up the CommentsRecyclerAdapter
        CommentsRecyclerAdapter commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentOptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentsRecyclerAdapter);

        // Start listening for changes in the Firestore data
        commentsRecyclerAdapter.startListening();
    }
}