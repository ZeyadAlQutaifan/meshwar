package com.meshwar.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.meshwar.meshwar.databinding.ActivityViewPlaceBinding;
import com.meshwar.meshwar.util.FireStore;

public class ViewPlaceActivity extends AppCompatActivity {
ActivityViewPlaceBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewPlaceBinding.inflate(getLayoutInflater()) ;
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("placeId")){
            String placeId = intent.getStringExtra("placeId");
            loadPlace(placeId) ;
        }
    }

    private void loadPlace(String placeId) {
        FireStore.getPLace(placeId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        binding.tvTitle.setText(documentSnapshot.getString("title"));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}