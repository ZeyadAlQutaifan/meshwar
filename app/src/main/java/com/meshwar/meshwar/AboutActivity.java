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
                 .setDescription("Tourism in general is one of the most important sources of income for the state, specifically in Jordan due to the large number of archaeological tourist places such as Petra, natural tourism such as Wadi Rum, medical tourism such as the Dead Sea and many others, but the problem for tourists lies in the lack of anything to guide them to some places that are difficult to find due to their inability to communicate with people easily due to language barriers and differences between what is available of pictures on the Internet and the place on the ground as well as changes in weather conditions and other things.\n" +
                                "\n" +
                                "Therefore, we provide a solution to these problems through our Meshwar application, which enables customers to obtain all the places and information about tourist sites in Jordan, obtain weather information during their trip, and give suggested plans for the places to visit in the appropriate order based on the time, current weather and other matters.   \n" +
                                "\n")
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