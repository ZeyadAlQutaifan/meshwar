package com.meshwar.meshwar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;
import com.github.dhaval2404.imagepicker.listener.DismissListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.meshwar.meshwar.databinding.ActivityPickImageBinding;
import com.meshwar.meshwar.models.User;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStorage;
import com.meshwar.meshwar.util.FireStore;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class PickImageActivity extends AppCompatActivity {
    ActivityPickImageBinding binding;
    private static final int PROFILE_IMAGE_REQ_CODE = 100;
    private Uri mProfileUri;

    private String password;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPickImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        password = getIntent().getStringExtra("PASSWORD");
    }

    public void pickProfileImage(View view) {
        ImagePicker.with(this)
                // Crop Square image
                .cropSquare()
                .setImageProviderInterceptor(new Function1<ImageProvider, Unit>() {
                    @Override
                    public Unit invoke(ImageProvider imageProvider) {
                        Log.d("ImagePicker", "Selected ImageProvider: " + imageProvider.toString());
                        return null;
                    }
                }).setDismissListener(new DismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.d("ImagePicker", "Dialog Dismiss");
                    }
                })
                // Image resolution will be less than 512 x 512
                .maxResultSize(200, 200)
                .start(PROFILE_IMAGE_REQ_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            Uri uri = data.getData();
            if (requestCode == PROFILE_IMAGE_REQ_CODE) {
                mProfileUri = uri;
                binding.imgProfile.setImageURI(mProfileUri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    public void createUser(View view) {
        if (mProfileUri == null) {
            Toast.makeText(this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            return;
        }
        FireAuth.createUser(User.getInstance().getEmail(), password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                uploadImage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PickImageActivity.this, "Error while creating account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        String userId = FireAuth.authInstance.getUid();
        FireStorage.uploadImage(mProfileUri, userId)
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        User.getInstance().setImageUrl(s);
                        uploadUser();
                    }
                });
    }

    private void uploadUser() {
        String userId = FireAuth.authInstance.getUid();

        FireStore.writeUser(userId, User.getInstance())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(PickImageActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // Finish the current activity
                    }
                });
    }
}