package com.biyonikhamza.prostheticsbookjava.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.biyonikhamza.prostheticsbookjava.R;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();

        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this , ProstheticShow.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}