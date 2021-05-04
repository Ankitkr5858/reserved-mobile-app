package com.articles.app.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.articles.app.R;
import com.articles.app.signup.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Animation for Splash Logo text
        TextView textView = findViewById(R.id.splashtxt);
        textView.setText(R.string.splash_text);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
        textView.startAnimation(animation);

        // Animation for Splash Logo img
        ImageView imageView = findViewById(R.id.splashlogo);
        imageView.setImageResource(R.drawable.splashlogo);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        imageView.startAnimation(anim);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}