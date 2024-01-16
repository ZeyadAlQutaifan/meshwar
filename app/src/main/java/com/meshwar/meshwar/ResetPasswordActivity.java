package com.meshwar.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.meshwar.meshwar.databinding.ActivityResetPasswordBinding;
import com.meshwar.meshwar.util.Constant;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.Global;

public class ResetPasswordActivity extends AppCompatActivity {
    ActivityResetPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    public void requestPasswordLink(View view) {
        String email = binding.etEmailInput.getText().toString().trim();
        if (!Global.isValidEmail(email)) {
            binding.etEmail.setError("Enter a valid email");
            binding.etEmail.requestFocus();
            return;
        }
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FireAuth.resetPassword(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                new MaterialAlertDialogBuilder(ResetPasswordActivity.this)
                        .setTitle("Reset Password Link has been sent to your email")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                new MaterialAlertDialogBuilder(ResetPasswordActivity.this)
                        .setTitle(e.getMessage())
                        .setPositiveButton("ok",null)
                        .setCancelable(false)
                        .show();
            }
        });
    }
}