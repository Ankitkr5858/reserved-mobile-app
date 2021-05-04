package com.articles.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.articles.app.Adapters.ArticlesViewPager;
import com.articles.app.Models.Datum;
import com.articles.app.signup.LoginActivity;
import com.articles.app.ui.UpdateActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetails extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.viewPager) ViewPager viewPager;

    private ArticlesViewPager mArticlesViewPager;
    List<Datum> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_fragment);

        ButterKnife.bind(this);

//                // Bottom Navigation!
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        // Set Home Screen as selected
        bottomNavigationView.setSelectedItemId(R.id.navigation_explore);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_posts:
                        startActivity(new Intent(getApplicationContext(), ContentList.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_explore:
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



        articles = Parcels.unwrap(getIntent().getParcelableExtra("articles"));
        int startingPosition = getIntent().getIntExtra("position", 0);

        mArticlesViewPager = new ArticlesViewPager(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, articles);
        viewPager.setAdapter(mArticlesViewPager);
        viewPager.setCurrentItem(startingPosition);
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
        Intent intent = new Intent(ArticleDetails.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}