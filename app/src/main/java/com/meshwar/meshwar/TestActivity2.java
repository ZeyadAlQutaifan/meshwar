package com.meshwar.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.meshwar.meshwar.databinding.ActivityTest2Binding;
import com.meshwar.meshwar.fragments.MainFragment;
import com.meshwar.meshwar.fragments.NearbyFragment;
import com.meshwar.meshwar.fragments.ProfileFragment;

public class TestActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityTest2Binding binding ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTest2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new MainFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new MainFragment()).commit();
        } else if (item.getItemId() == R.id.nav_my_post) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new NearbyFragment()).commit();
        } else if (item.getItemId() == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new ProfileFragment()).commit();
        } else if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if ( binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}