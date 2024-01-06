package com.meshwar.meshwar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.meshwar.meshwar.databinding.ActivityMainBinding;
import com.meshwar.meshwar.fragments.FavorateFragment;
import com.meshwar.meshwar.fragments.MainFragment;
import com.meshwar.meshwar.fragments.MyPostFragment;
import com.meshwar.meshwar.fragments.NearbyFragment;
import com.meshwar.meshwar.models.User;
import com.meshwar.meshwar.util.FireAuth;
import com.meshwar.meshwar.util.FireStore;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        getUserInfo();
        binding.navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        replaceFragment(new MainFragment());
        binding.floatingActionButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddPlaceActivity.class));
        });
//        binding.bottomNavigation.setOnItemSelectedListener(item -> {
//            binding.floatingActionButton.show();
//            if (item.getItemId() == R.id.navigation_home) {
//                replaceFragment(new MainFragment());
//                return true;
//            } else if (item.getItemId() == R.id.navigation_nearby) {
//                replaceFragment(new NearbyFragment());
//                return true;
//            } else if (item.getItemId() == R.id.navigation_fav) {
//                replaceFragment(new FavorateFragment());
//                return true;
//            } else if (item.getItemId() == R.id.navigation_profile) {
//                replaceFragment(new ProfileFragment());
//                return true;
//            }
//            return false;
//        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new MainFragment()).commit();
        } else if (item.getItemId() == R.id.nav_my_post) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new MyPostFragment()).commit();
        } else if (item.getItemId() == R.id.nav_favorite) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new FavorateFragment()).commit();
        }  else if (item.getItemId() == R.id.nav_nearby) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, new NearbyFragment()).commit();
        } else if (item.getItemId() == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this , AboutActivity.class));
        } else if (item.getItemId() == R.id.nav_logout) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Log out")
                    .setNegativeButton("No, stay", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                            finishAndRemoveTask();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view_tag, fragment);
        fragmentTransaction.commit();
    }
    private void getUserInfo(){
        String userId = FireAuth.authInstance.getUid();

        FireStore.getUser(userId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        User user = documentSnapshot.toObject(User.class);
                        View headerView = binding.navView.getHeaderView(0);

                        // Access and modify the TextView in the header
                        TextView txtUserName = headerView.findViewById(R.id.txtUserName);
                        TextView txtUserEmail = headerView.findViewById(R.id.txtUserEmail);
                        CircleImageView imgUser = headerView.findViewById(R.id.imgUser);
                        txtUserName.setText(user.getFullName());
                        txtUserEmail.setText(user.getEmail());
                        Glide.with(headerView)
                                .load(user.getImageUrl())
                                .into(imgUser);

                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        // Check if there are fragments in the back stack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Pop the back stack to go back to Fragment1
            getSupportFragmentManager().popBackStack();
        } else {
            // If no fragments in the back stack, proceed with the default behavior
            super.onBackPressed();
        }
    }
}