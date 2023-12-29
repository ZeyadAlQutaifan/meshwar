package com.meshwar.meshwar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class startpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
    }

    public void start(View view) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent=new Intent(startpage.this , MainActivity.class );

// intent to main

        }else{
            Intent intent=new Intent(startpage.this , LoginActivity.class);
// intent to login 
        }
        finish();
    }
}