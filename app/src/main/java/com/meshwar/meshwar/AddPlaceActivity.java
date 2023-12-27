package com.meshwar.meshwar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.meshwar.meshwar.adapters.RecyclerImageFromGalleryAdapter;
import com.meshwar.meshwar.databinding.ActivityAddPlaceBinding;
import com.meshwar.meshwar.databinding.ActivityMainBinding;
import com.meshwar.meshwar.fragments.MainFragment;
import com.meshwar.meshwar.models.Place;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStorage;
import com.meshwar.meshwar.util.FireStore;

import java.util.ArrayList;

public class AddPlaceActivity extends AppCompatActivity {

    ActivityAddPlaceBinding binding ;
    private static final int REQUEST_CODE = 1; // You can choose any request code

    ArrayList<Uri> chosenImagesUriList = new ArrayList<>();
    private RecyclerImageFromGalleryAdapter recyclerImageFromGalleryAdapter;
    private static final int READ_PERMISSION = 101;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initPickImagesRecyclerView();

    }
    private void initPickImagesRecyclerView() {

        recyclerImageFromGalleryAdapter = new RecyclerImageFromGalleryAdapter(chosenImagesUriList);
        binding.recyclerImagesFromGallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerImagesFromGallery.setAdapter(recyclerImageFromGalleryAdapter);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        }
        String[] municipalities = getResources().getStringArray(R.array.jordan_municipalities);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, municipalities);
        binding.etCity.setAdapter(adapter);
        binding.fabAddImages.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("image/*");

            pickMedia.launch(intent);
        });
        binding.btnPickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(AddPlaceActivity.this), REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    throw new RuntimeException(e);
                } catch (GooglePlayServicesNotAvailableException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binding.btnAddPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(AddPlaceActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                Place place = Place.getInstance();
                place.setLng(8.44);
                place.setLat(94.444);
                for (Uri uri : chosenImagesUriList) {
                    place.getImages().add(String.valueOf(uri));
                }
                place.setViews(0);
                place.setCity(binding.etCity.getText().toString());
                place.setRate(0.0);
                place.setDescription(binding.etDescription.getText().toString());
                place.setTitle(binding.etTitle.getText().toString());
                place.setCreatedAt(System.currentTimeMillis());
                place.setWriterId(FireAuth.authInstance.getCurrentUser().getUid());

                FireStorage.uploadImages(chosenImagesUriList, binding.etTitle.getText().toString()).addOnSuccessListener(downloadUrls -> {
                    // downloadUrls is a List<String> containing the download URLs for all uploaded images

                    // Save the post to Firestore with the download URLs
                    FireStore.addPlace(downloadUrls).addOnSuccessListener(documentReference -> {
                        progressDialog.dismiss();
                        Place.destroy();
                        finishAndGoBackToManin();
                    }).addOnFailureListener(e -> {
                        // Handle the failure to save post to Firestore
                    });
                }).addOnFailureListener(e -> {
                    // Handle the failure to upload images
                });


            }
        });
    }
    private void finishAndGoBackToManin() {
       finish();
    }
    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            chosenImagesUriList.clear();
                            // Single image selected
                            Uri selectedImageUri = data.getData();
                            chosenImagesUriList.add(selectedImageUri);
                        } else if (data.getClipData() != null) {
                            // Multiple images selected
                            ClipData clipData = data.getClipData();
                            if (clipData.getItemCount() < 11) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    Uri selectedImageUri = clipData.getItemAt(i).getUri();
                                    chosenImagesUriList.add(selectedImageUri);
                                }
                            } else {
                                Toast.makeText(this, "Not allowed to pick more than 10 images", Toast.LENGTH_LONG).show();
                            }
                        }
                        recyclerImageFromGalleryAdapter.notifyDataSetChanged();

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {

            } else {

            }
        }
    });

}