package com.articles.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.articles.app.Adapters.ArticleAdapter;
import com.articles.app.Models.ArticlesResponse;
import com.articles.app.Models.Datum;
import com.articles.app.Network.ArticlesApi;
import com.articles.app.Network.ArticlesClient;
import com.articles.app.signup.LoginActivity;
import com.articles.app.ui.UpdateActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreScreen extends AppCompatActivity implements View.OnClickListener {

    private ArticleAdapter articleAdapter;
    public List<Datum> articles;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.errorTextView) TextView errorTextView;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_load);
        ButterKnife.bind(this);

        // Bottom Navigation!
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

        //Establishing Connection to the Model classes.
        ArticlesApi client = ArticlesClient.getClient();

        Call<ArticlesResponse> call = client.getArticles("9939919a16f6e777548bfbd8cec327ca");
        call.enqueue(new Callback<ArticlesResponse>() {
            @Override
            public void onResponse(@NotNull retrofit2.Call<ArticlesResponse> call, @NotNull Response<ArticlesResponse> response) {
                if (response.isSuccessful()) {
                    hideProgressBar();
//                    assert response.body() != null;
                    assert response.body() != null;
                    articles = response.body().getData();
                    articleAdapter = new ArticleAdapter(ExploreScreen.this, articles);
                    recyclerView.setAdapter(articleAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ExploreScreen.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    showArticles();
                } else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(@NotNull retrofit2.Call<ArticlesResponse> call, @NotNull Throwable t) {
                hideProgressBar();
                showFailureMessage();

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
        Intent intent = new Intent(ExploreScreen.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showArticles() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showFailureMessage() {
        errorTextView.setText(R.string.FailureMsg);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showUnsuccessfulMessage() {
        errorTextView.setText(R.string.UnsuccessfulMsg);
        errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {

    }
}


