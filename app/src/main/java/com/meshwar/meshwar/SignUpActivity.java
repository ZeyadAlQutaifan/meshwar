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
import com.meshwar.meshwar.util.Global;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void callNextSigupScreen(View view) {
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();
        FireAuth.createUser(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                User newUser = User.getInstance();
                newUser.setEmail(email);
                newUser.setFullName(fullName);
                newUser.setCreationDate(System.currentTimeMillis());
                FireStore.writeUser(authResult.getUser().getUid(), newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    }
                });
            }
        });
    }

    public void continueSignup(View view) {
        String fullName = binding.etFullName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString();
        String confirmPassword = binding.etConfirmPassword.getText().toString();
        if (fullName.isEmpty()) {
            binding.etFullName.setError("Required");
            binding.etFullName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Required");
            binding.etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.etEmail.setError("Required");
            binding.etEmail.requestFocus();
            return;
        }

        if (!Global.isValidEmail(email)) {
            binding.etEmail.setError("Invalid email");
            binding.etEmail.requestFocus();
            return;
        }
        if (password.length() <8) {
            binding.etPassword.setError("Must be more that 8 ");
            binding.etPassword.requestFocus();
            return;
        }
        if (password.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Must be more that 8 ");
            binding.etConfirmPassword.requestFocus();
            return;
        }


        User.getInstance().setEmail(email);
        User.getInstance().setFullName(fullName);
        User.getInstance().setCreationDate(System.currentTimeMillis());
        startActivity(new Intent(this, PickImageActivity.class));


    }

    public void back(View view) {
        finish();
    }
}