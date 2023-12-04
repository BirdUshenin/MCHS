package com.example.mchs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    // Время отображения splash screen в миллисекундах
    private static final int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Используем Handler для задержки и перехода к основной активности
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Запуск основной активности после задержки
                Intent mainIntent = new Intent(SplashScreenActivity.this, ProfileActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}