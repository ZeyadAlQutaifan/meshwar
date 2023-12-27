package com.meshwar.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.meshwar.meshwar.databinding.ActivityMainBinding;
import com.meshwar.meshwar.databinding.ActivitySignUpBinding;
import com.meshwar.meshwar.models.User;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStore;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void callNextSigupScreen(View view) {
        String fullName = binding.etFullName.getText().toString().trim() ;
        String email = binding.etEmail.getText().toString().trim() ;
        String password = binding.etPassword.getText().toString() ;
        FireAuth.createUser(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User newUser = new User() ;
                newUser.setEmail(email);
                newUser.setFullName(fullName);
                newUser.setCreationDate(System.currentTimeMillis());
                FireStore.writeUser(authResult.getUser().getUid() ,newUser ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(SignUpActivity.this , MainActivity.class));
                    }
                });
            }
        });
    }
}