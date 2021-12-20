package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DetailKnihy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_knihy);
        Bundle extras = getIntent().getExtras();
        String id = "";
        if (extras != null) {
             id = extras.getString("key");
        }


    }
}