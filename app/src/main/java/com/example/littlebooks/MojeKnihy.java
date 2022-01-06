package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.littlebooks.old.BooksActivity;
import com.example.littlebooks.old.MainActivity;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MojeKnihy extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    FavoriteAdapter adapter;

    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moje_knihy);

        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerViewBooksFavorite);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mbase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<FavoriteBook> options
                = new FirebaseRecyclerOptions.Builder<FavoriteBook>()
                .setQuery(mbase.child("Books"), FavoriteBook.class)
                .build();

        adapter = new FavoriteAdapter(options, MojeKnihy.this);

        recyclerView.setAdapter(adapter);
    }

    @Override protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override protected void onStop(){
        super.onStop();
        adapter.stopListening();
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

    public void ClickMojeKnihy(View view){recreate();}

    public void ClickDomov(View view){MainActivity.redirectActivity(this, MainActivity.class);}


    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}