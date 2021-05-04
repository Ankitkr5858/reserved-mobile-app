package com.articles.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.articles.app.Adapters.CustomAdapter;
import com.articles.app.Adapters.FirebaseAdapter;
import com.articles.app.Fragments.ContentListFragment;
import com.articles.app.Fragments.FragmentDetails;
import com.articles.app.Fragments.UpdateListFragment;
import com.articles.app.Models.PostInfo;
import com.articles.app.interfaces.Display_interface;
import com.articles.app.signup.LoginActivity;
import com.articles.app.ui.UpdateActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.ButterKnife;

public class ContentList extends AppCompatActivity{
    private ContentListFragment contentListFragment;
    private FrameLayout fragmentList;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_list);
        ButterKnife.bind(this);

        fragmentList = findViewById(R.id.second_container);
//        fragmentUpdate = findViewById(R.id.fragment_detail);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ContentListFragment updateListFragment = new ContentListFragment();

        fragmentTransaction.add(fragmentList.getId(), updateListFragment).commit();


        // Bottom Navigation!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // Set Home Screen as selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_posts);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_posts:
                        return true;

                    case R.id.navigation_explore:
                        startActivity(new Intent(getApplicationContext(), ExploreScreen.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_update:
                        startActivity(new Intent(getApplicationContext(), UpdateActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_upload:
                        startActivity(new Intent(getApplicationContext(), UploadContent.class));
                        overridePendingTransition(0, 0);
                        return true;
                }

                return false;
            }
        });

    }

    // Logout from MyWiki
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ContentList.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}