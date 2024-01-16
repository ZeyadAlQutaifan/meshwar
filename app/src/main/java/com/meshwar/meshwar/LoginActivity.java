package com.meshwar.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.meshwar.meshwar.databinding.ActivityLoginBinding;
import com.meshwar.meshwar.databinding.ActivityMainBinding;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.Global;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        binding.btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        binding.txtResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , ResetPasswordActivity.class));
            }
        });
    }

    private void login() {
        String email = binding.loginEmailEditText.getText().toString().trim();
        String password = binding.loginPasswordEditText.getText().toString().trim();
        if (email.isEmpty()) {
            binding.loginEmailEditText.setError("Required");
            binding.loginEmailEditText.requestFocus();
            return;
        }
        if (!Global.isValidEmail(email)) {
            binding.loginEmailEditText.setError("Invalid email");
            binding.loginEmailEditText.requestFocus();
            return;
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FireAuth.login(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(LoginActivity.this , MainActivity.class));
                        finish();
                        progressDialog.dismiss();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }
}