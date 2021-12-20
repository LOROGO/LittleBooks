package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.littlebooks.ui.main.Test02Fragment;

public class test02 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test02_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Test02Fragment.newInstance())
                    .commitNow();
        }
    }
}