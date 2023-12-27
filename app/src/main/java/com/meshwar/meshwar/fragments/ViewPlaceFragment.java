package com.meshwar.meshwar.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.meshwar.meshwar.R;
import com.meshwar.meshwar.adapters.CommentsRecyclerAdapter;
import com.meshwar.meshwar.databinding.FragmentViewPlaceBinding;
import com.meshwar.meshwar.models.Comment;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStore;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;


public class ViewPlaceFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    List<SlideModel> imageList = new ArrayList<SlideModel>();

    FragmentViewPlaceBinding binding;
    private static final String ARG_PLACE_ID = "param1";

    private String placeId;

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
        binding.mapView2.getMapAsync(this);
        binding.btnLike.setCheckable(true);

        binding.txtCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        FireStore.getPLace(placeId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Place place = documentSnapshot.toObject(Place.class);
                for (String uri : place.getImages()) {
                    imageList.add(new SlideModel(uri, null, ScaleTypes.CENTER_CROP));
                }
                binding.imageSlider.setImageList(imageList);
                binding.txtTitle.setText(place.getTitle());
                binding.txtDescription.setText(place.getDescription());
                int commentCount = 0;
                FireStore.getPlaceComments(placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            binding.txtCommentCount.setText(task.getResult().getDocuments().size());

                        }
                    }
                });
                // Set Location on the map
                updateLocation(new LatLng(place.getLat(), place.getLng()) , 18);
            }
        });
        return binding.getRoot();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    private void updateLocation(LatLng location, float zoomLevel) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
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

    private void showDialog() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.comments_bottom_sheet_layout);
        TextInputEditText etComment = dialog.findViewById(R.id.etComment);
        Button btnSend = dialog.findViewById(R.id.btnSend);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);

        loadComments(recyclerView);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etComment.getText().toString();
                Comment comment = new Comment();
                comment.setBody(body);
                comment.setPlaceId(placeId);
                comment.setCreationTime(System.currentTimeMillis());
                comment.setOwnerID(FireAuth.authInstance.getUid());

                FireStore.addComment(comment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        etComment.setText("");

                        loadComments(recyclerView);

                    }
                });
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private void loadComments(RecyclerView recyclerView) {
        Query query = FireStore.getPlaceComments(placeId);
        FirestoreRecyclerOptions<Comment> commentOptions = new FirestoreRecyclerOptions.Builder<Comment>()
                .setQuery(query, Comment.class)
                .build();

        CommentsRecyclerAdapter commentsRecyclerAdapter = new CommentsRecyclerAdapter(commentOptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(commentsRecyclerAdapter);
        commentsRecyclerAdapter.startListening();
    }
}