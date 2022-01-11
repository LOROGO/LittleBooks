package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashAcitivity extends AppCompatActivity {
    Animation topanim;
    ImageView knihy;

    private static int Splash_time = 1000; //3000

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //splashactivity na fullscreen
        setContentView(R.layout.activity_splash_acitivity);

        topanim = AnimationUtils.loadAnimation(this, R.anim.topanim);
        knihy = findViewById(R.id.knihy);

        knihy.setAnimation(topanim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent(SplashAcitivity.this, Login.class);
                startActivity(intent);
            }
        }, Splash_time);
    }
}