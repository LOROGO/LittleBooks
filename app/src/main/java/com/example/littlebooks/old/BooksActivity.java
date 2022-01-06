package com.example.littlebooks.old;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.littlebooks.Account;
import com.example.littlebooks.MojeKnihy;
import com.example.littlebooks.NewBook;
import com.example.littlebooks.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BooksActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    ImageButton searchBtn;
    EditText searchBar;
    BookAdapter adapter;
    DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        searchBar = findViewById(R.id.search_bar_book);
        searchBtn = findViewById(R.id.search_button_book);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerViewBooks);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mbase = FirebaseDatabase.getInstance("https://kniznicaprosim-default-rtdb.firebaseio.com/").getReferenceFromUrl("https://kniznicaprosim-default-rtdb.firebaseio.com/");

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FirebaseRecyclerOptions<Books> options
                = new FirebaseRecyclerOptions.Builder<Books>()
                .setQuery(mbase.child("Books"), Books.class)
                .build();

        adapter = new BookAdapter(options, BooksActivity.this);
        recyclerView.setAdapter(adapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nazov = searchBar.getText().toString();

                FirebaseRecyclerOptions<Books> options
                        = new FirebaseRecyclerOptions.Builder<Books>()
                        .setQuery(mbase.child("Books").orderByChild("nazov").startAt(nazov).endAt(nazov+"\uf8ff"), Books.class)
                        .build();

                adapter.updateOptions(options);
            }
        });
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
        recreate();
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

    public void ClickDomov(View view){MainActivity.redirectActivity(this, MainActivity.class);}

    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}