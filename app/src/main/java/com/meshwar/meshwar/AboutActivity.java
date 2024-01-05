package com.meshwar.meshwar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import husaynhakeem.com.aboutpage.AboutPage;
import husaynhakeem.com.aboutpage.Item;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                new AboutPage(this)
                .setBackground(android.R.color.white)
                .setImage(R.mipmap.ic_launcher)
                        //.setDescription("Hello")
                .addItem(new Item("Meshwar Version 1.0" , null, null))
                .addEmail("husaynhakeem@gmail.com")
                .addFacebook("linkinpark")
                .addGithub("husaynhakeem")
                .addInstagram("husaynhakeem")
                .addPlayStore("com.maketrumptweetseightagain")
                .addTwitter("oneplus")
                .addWebsite("http://www.google.com")
                .addYoutube("UCyWqModMQlbIo8274Wh_ZsQ")
                .create());
    }
}