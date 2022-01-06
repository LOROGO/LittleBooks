package com.example.littlebooks.old;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.littlebooks.Account;
import com.example.littlebooks.MojeKnihy;
import com.example.littlebooks.NewBook;
import com.example.littlebooks.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Obsah extends AppCompatActivity {
    TextView obsah, nazov;
    DatabaseReference mbase;
    String id;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obsah);

        drawerLayout = findViewById(R.id.drawer_layout);
        obsah = findViewById(R.id.obsah);
        nazov = findViewById(R.id.nazov);
        id = getIntent().getStringExtra("Nazov");

        mbase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/").child("Books").child(id);
        mbase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                obsah.setText(snapshot.child("obsah").getValue().toString());
                nazov.setText(snapshot.child("meno").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickBooks(View view){
        MainActivity.redirectActivity(this, BooksActivity.class);
    }

    public void ClickAccount(View view){
        MainActivity.redirectActivity(this, Account.class);
    }

    public void ClickNewBook(View view){
        MainActivity.redirectActivity(this, NewBook.class);
    }

    public void ClickLogout(View view){
        MainActivity.logout(this);
    }

    public void ClickMojeKnihy(View view){MainActivity.redirectActivity(this, MojeKnihy.class);}

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}
