package com.articles.app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.articles.app.ContentList;
import com.articles.app.ExploreScreen;
import com.articles.app.Fragments.FragmentDetails;
import com.articles.app.Fragments.UpdateListFragment;
import com.articles.app.R;
import com.articles.app.UploadContent;
import com.articles.app.interfaces.Display_interface;
import com.articles.app.signup.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.ButterKnife;

public class UpdateActivity extends AppCompatActivity{
    private  FrameLayout fragmentList, fragmentUpdate;
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);

        fragmentList = findViewById(R.id.fragment_container_view_tag);
//        fragmentUpdate = findViewById(R.id.fragment_detail);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        UpdateListFragment updateListFragment = new UpdateListFragment();

        fragmentTransaction.add(fragmentList.getId(), updateListFragment).commit();

        // Bottom Navigation!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // Set Home Screen as selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_update);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_posts:
                        startActivity(new Intent(getApplicationContext(), ContentList.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.navigation_explore:
                        startActivity(new Intent(getApplicationContext(), ExploreScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_update:
                        return true;

                    case R.id.navigation_upload:
                        startActivity(new Intent(getApplicationContext(), UploadContent.class));
                        overridePendingTransition(0,0);
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
        Intent intent = new Intent(UpdateActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
//    @Override
//    public void onVersionItemClicked(String s) {
//        Toast.makeText(this,"Hey", Toast.LENGTH_SHORT).show();
//        Bundle bundle = new Bundle();
//        bundle.putString("first_name", s);
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        FragmentDetails detail_fragment = new FragmentDetails();
//        detail_fragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.fragment_detail, detail_fragment);
//        fragmentTransaction.commit();
//    }
