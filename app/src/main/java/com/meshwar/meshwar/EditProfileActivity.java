package com.meshwar.meshwar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.meshwar.meshwar.databinding.ActivityEditProfileBinding;
import com.meshwar.meshwar.models.User;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStorage;
import com.meshwar.meshwar.util.FireStore;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;
    String userImageUri;
    Uri newUserImageUri;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FireStore.getUser(FireAuth.authInstance.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        progressDialog.dismiss();
                        User user = documentSnapshot.toObject(User.class);
                        fillUserInfo(user);
                    }
                }
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("Save")
                        .setMessage("Are you sure you want to update")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                updateUserInfo();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        binding.tvAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                pickMedia.launch(intent);
            }
        });
        setContentView(binding.getRoot());
    }

    private void updateUserInfo() {
        progressDialog.show();
        String username = Objects.requireNonNull(binding.txtUserName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.txtUserEmail.getText()).toString().trim();
        if (username.isEmpty()) {
            binding.txtUserName.setError("Required!");
            binding.txtUserName.requestFocus();
            return;
        }

        User newUser = User.getInstance();
        newUser.setFullName(username);
        newUser.setEmail(email);

        if (newUserImageUri != null) {
            FireStorage.uploadImage(newUserImageUri, FireAuth.authInstance.getUid()).addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    newUser.setImageUrl(s);
                    FireStore.updateUser(FireAuth.authInstance.getUid(), newUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    finish();
                                }
                            });
                }
            });
        } else {
            newUser.setImageUrl(userImageUri);
            FireStore.updateUser(FireAuth.authInstance.getUid(), newUser)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            finish();
                        }
                    });
        }
    }

    private void fillUserInfo(User user) {
        userImageUri = user.getImageUrl();
        binding.txtUserName.setText(user.getFullName());
        binding.txtUserEmail.setText(user.getEmail());
        binding.btnSave.setEnabled(true);
        binding.txtUserEmail.setEnabled(false);
        Glide.with(this)
                .load(user.getImageUrl())
                .into(binding.userImage);

    }

    ActivityResultLauncher<Intent> pickMedia =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        if (data.getData() != null) {
                            // Single image selected
                            Uri selectedImageUri = data.getData();
                            newUserImageUri = selectedImageUri;
                            binding.userImage.setImageURI(newUserImageUri);
                        }

                    }
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

}